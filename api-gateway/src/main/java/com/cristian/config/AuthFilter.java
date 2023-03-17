package com.cristian.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
//                log.info("Query parameters: " + queryParams.toString());
                List<String> tokenValue = queryParams.get("token");
//                log.info("Token value from query parameters: " + tokenValue.toString());
                if (tokenValue == null || tokenValue.isEmpty()) {
                    throw new RuntimeException("Missing authorization information");
                } else {
                    exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue.get(0)).build();
                }
//                log.info(String.valueOf(exchange.getRequest().getHeaders()));

            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            String[] parts = authHeader.split(" ");

            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                throw new RuntimeException("Incorrect authorization structure");
            }

            return webClientBuilder.build()
                    .post()
                    .uri("http://AUTHENTICATION-SERVICE/auth/validateToken?token=" + parts[1])
                    .retrieve().bodyToMono(String.class)
                    .map(email -> {
                        exchange.getRequest()
                                .mutate()
                                .header("email", String.valueOf(email))
                                .build();
                        log.info(email);
                        log.info(String.valueOf(exchange.getRequest().getHeaders()));

                        return exchange;
                    })
                    .flatMap(chain::filter)
                    .onErrorResume(ex -> {
                        log.error("Error occurred while validating token: " + ex.getMessage());
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });

        };
    }

    public static class Config {
        // empty class as I don't need any particular configuration
    }
}

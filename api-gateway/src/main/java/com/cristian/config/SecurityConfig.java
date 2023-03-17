package com.cristian.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

//        http
//                .cors().configurationSource(request -> {
//                    CorsConfiguration configuration = new CorsConfiguration();
//                    configuration.setAllowedOrigins(List.of("http://localhost:3001"));
//                    configuration.setAllowedMethods(List.of("*"));
//                    configuration.setAllowedHeaders(List.of("*"));
//                    configuration.setAllowCredentials(true);
//                    return configuration;
//                });

        return http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/auth/**","/transactions","/notifications/**").permitAll() // allow access to public routes
//                .anyExchange().authenticated() // require authentication for all other routes
                .and()
                .build();
    }

}


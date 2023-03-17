package com.cristian.security;

import com.cristian.user.UserEntity;
import com.cristian.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository
                .findOneByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " does not exist"));
        return new UserDetailsImp(user);
    }
}

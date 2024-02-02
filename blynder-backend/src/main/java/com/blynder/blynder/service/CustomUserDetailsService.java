package com.blynder.blynder.service;


import com.blynder.blynder.exception.UserIsBannedException;
import com.blynder.blynder.model.User;
import com.blynder.blynder.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(s));
        if (user.isBanned()) {
            if (user.getBannedUntil().before(new Date())) {
                user.setBanned(false);
                user.setBannedUntil(null);
                userRepository.save(user);
            }
            else throw new UserIsBannedException("User is banned until "+ user.getBannedUntil());
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getAuthorities()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}

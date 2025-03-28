package com.example.usedcarportal.service;

import com.example.usedcarportal.model.User;
import com.example.usedcarportal.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Load user by username (email) and return UserDetails for authentication
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email) // Fetch user from the repository using email
                .orElseThrow(() -> new UsernameNotFoundException("User not found")); // Throw exception if user not found

        // Return a UserDetails object with the user's email, password, and roles
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Set email as username
                user.getPassword(), // Set password
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())) // Assign role to the user
        );
    }
}

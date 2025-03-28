package com.example.usedcarportal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Configures security settings for HTTP requests, authentication, and login.
@Configuration
public class SecurityConfig {

    // Configures HTTP security settings.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Permit access to the register, login, and static assets
                .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
                // Restrict admin URLs to users with the ROLE_ADMIN authority
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                // Require authentication for all other requests
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                // Custom login page
                .loginPage("/login")
                // Redirect users after successful login based on role
                .successHandler(customSuccessHandler())
                .permitAll()
            )
            .logout(logout -> logout
                // Custom logout URL and redirect after successful logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build(); // Builds the security configuration
    }

    // Provides a password encoder using BCrypt.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Custom authentication success handler to redirect users based on their role.
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                                Authentication authentication) throws IOException, ServletException {
                // Debugging: Print the user's authorities
                System.out.println("User Authorities: " + authentication.getAuthorities());

                // Redirect Admins to the admin dashboard
                if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                    response.sendRedirect("/admin/dashboard");
                } else {
                    // Redirect regular users to the home page
                    response.sendRedirect("/home");
                }
            }
        };
    }
}

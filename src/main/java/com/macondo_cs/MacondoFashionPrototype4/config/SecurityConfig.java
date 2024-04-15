package com.macondo_cs.MacondoFashionPrototype4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.macondo_cs.MacondoFashionPrototype4.services.NewUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    // creates a user and saves it to the app memory
    @Bean
    public UserDetailsService userDetailsService() {
        return new NewUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                .requestMatchers(
                    "/login", "/register", "/register/save",
                    "/{sex}", "/{sex}/{productName}",
                    "/{sex}/{productName}/{productId}",
                    "/error", "/about", "/contact", "/").permitAll()
                        .requestMatchers("/**").authenticated())
                //     .requestMatchers("/**").authenticated())
                // .authorizeHttpRequests(auth -> auth.requestMatchers("/new-user", "/all-users").hasRole("ADMIN"))
                .formLogin(login -> login.loginPage("/login")) // AbstractAuthenticationFilterConfigurer::permitAll
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}

package com.iuh.fit.se.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnProperty(name = "security.oauth2.client.enabled", havingValue = "true")
public class GoogleOAuth2SecurityConfig {

    @Bean
    public SecurityFilterChain oauth2LoginFilterChain(HttpSecurity http) throws Exception {
        http
     
            .securityMatcher("/**")
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/api/public/**", "/oauth2/**", "/login/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                .loginPage("/oauth2/authorization/google")
              
                .defaultSuccessUrl("http://localhost:5173/login/callback", true)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
            );
        return http.build();
    }
}



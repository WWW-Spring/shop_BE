package com.iuh.fit.se.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnProperty(name = "security.oauth2.client.enabled", havingValue = "true")
public class OAuth2ClientSecurityConfig {

	@Bean
	public SecurityFilterChain oauth2ClientFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login/oauth2/**", "/oauth2/**", "/api/public/**", "/actuator/**").permitAll()
						.anyRequest().authenticated()
				)
				.oauth2Login(Customizer.withDefaults());
		return http.build();
	}
}



package com.iuh.fit.se.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	@ConditionalOnProperty(name = "security.oauth2.enabled", havingValue = "true")
	public SecurityFilterChain securityFilterChainOAuth2(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(jwt -> extractAuthoritiesFromJwt(jwt));

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // public endpoints and docs
                        .requestMatchers("/api/public/**", "/api/auth/**", "/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // allow browsing products/categories/statistics/reviews without account
                        .requestMatchers(org.springframework.http.HttpMethod.GET, 
                                "/api/products/**", 
                                "/api/categories/**", 
                                "/api/statistics/**", 
                                "/api/reviews/**").permitAll()
                      
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                      
                        .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN", "MANAGER")
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));
		return http.build();
	}

	@Bean
	@ConditionalOnProperty(name = "security.oauth2.enabled", havingValue = "false", matchIfMissing = true)
	public SecurityFilterChain securityFilterChainOpen(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		config.setExposedHeaders(List.of("Authorization"));
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

    private Collection<GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
		// Support common providers (Keycloak realm_access.roles, resource_access roles, or a generic 'roles' claim)
        var authorities = new java.util.HashSet<GrantedAuthority>();
		Object realmAccess = jwt.getClaim("realm_access");
		if (realmAccess instanceof Map<?, ?> realm) {
			Object roles = realm.get("roles");
			if (roles instanceof List<?> list) {
				authorities.addAll(list.stream()
						.filter(String.class::isInstance)
						.map(String.class::cast)
						.map(r -> new SimpleGrantedAuthority("ROLE_" + r))
						.collect(Collectors.toSet()));
			}
		}
		Object genericRoles = jwt.getClaim("roles");
		if (genericRoles instanceof List<?> list) {
			authorities.addAll(list.stream()
					.filter(String.class::isInstance)
					.map(String.class::cast)
					.map(r -> new SimpleGrantedAuthority("ROLE_" + r))
					.collect(Collectors.toSet()));
		}
		return authorities;
	}
}



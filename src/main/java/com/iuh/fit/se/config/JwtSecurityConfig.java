package com.iuh.fit.se.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnProperty(name = "security.jwt.enabled", havingValue = "true")
public class JwtSecurityConfig {

	@Bean
	public SecurityFilterChain jwtFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/public/**", "/api/auth/**").permitAll()
						.anyRequest().authenticated()
				)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public JwtAuthFilter jwtAuthFilter(@Value("${security.jwt.secret}") String secret) {
		return new JwtAuthFilter(secret);
	}

	static class JwtAuthFilter extends OncePerRequestFilter {
		private final Algorithm algorithm;
		JwtAuthFilter(String secret) { this.algorithm = Algorithm.HMAC256(secret); }
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
			String auth = request.getHeader("Authorization");
			if (auth != null && auth.startsWith("Bearer ")) {
				try {
					String token = auth.substring(7);
					var verifier = JWT.require(algorithm).build();
					var decoded = verifier.verify(token);
					String subject = decoded.getSubject();
					List<String> roles = decoded.getClaim("roles").asList(String.class);
					var authorities = roles == null ? List.<SimpleGrantedAuthority>of() : roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toList());
					var authToken = new UsernamePasswordAuthenticationToken(subject, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} catch (Exception ignored) {}
			}
			filterChain.doFilter(request, response);
		}
	}
}



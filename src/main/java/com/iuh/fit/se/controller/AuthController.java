package com.iuh.fit.se.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.iuh.fit.se.domain.entity.User;
import com.iuh.fit.se.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class AuthController {

    private final UserRepository userRepository;
    @Value("${security.jwt.secret}")
    private String jwtSecret;
    @Value("${security.jwt.expiration-seconds}")
    private long jwtExpSeconds;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.getOrDefault("username", "");
        String password = body.getOrDefault("password", "");
        User user = userRepository.findByEmail(username)
                .or(() -> userRepository.findByPhoneNumber(username))
                .orElse(null);
        if (user == null || user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }
        String role = user.getRole() != null ? user.getRole().getName() : "USER";
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        Instant now = Instant.now();
        String token = JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withClaim("email", user.getEmail())
                .withClaim("phone", user.getPhoneNumber())
                .withClaim("roles", List.of(role))
                .withIssuedAt(now)
                .withExpiresAt(now.plusSeconds(jwtExpSeconds))
                .sign(algorithm);
        Map<String, Object> res = new HashMap<>();
        res.put("accessToken", token);
        res.put("tokenType", "Bearer");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(
            Authentication authentication,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        // Nếu là OIDC (Google) thì trả thông tin chi tiết
        if (authentication != null && authentication.getPrincipal() instanceof OidcUser oidc) {
            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "name", oidc.getFullName(),
                    "email", oidc.getEmail(),
                    "picture", oidc.getPicture(),
                    "claims", oidc.getClaims()
            ));
        }
        // Nếu client đang dùng Bearer JWT tùy chỉnh
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return ResponseEntity.ok(Map.of("authenticated", true));
        }
        return ResponseEntity.status(401).body(Map.of("authenticated", false));
    }
}



package fr.croixrouge.exposition.controller;


import fr.croixrouge.config.JwtTokenConfig;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.exposition.dto.LoginRequest;
import fr.croixrouge.exposition.dto.LoginResponse;
import fr.croixrouge.model.UserSecurity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenConfig jwtTokenConfig;
    private final SecretKey secretKey;

    public LoginController(AuthenticationManager authenticationManager, JwtTokenConfig jwtTokenConfig, SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenConfig = jwtTokenConfig;
        this.secretKey = secretKey;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            UserSecurity user = (UserSecurity) authentication.getPrincipal();
            String jwtToken = generateJwtToken(user);

            return ResponseEntity.ok(new LoginResponse(jwtToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private String generateJwtToken(UserSecurity user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtTokenConfig.getTokenExpiration());

        List<Map<String, String>> authorities = user.getAuthorities().stream()
                .map(authority -> Collections.singletonMap("authority", authority.getAuthority()))
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getUserId())
                .claim("authorities", authorities)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}

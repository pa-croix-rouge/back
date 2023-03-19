package fr.croixrouge.service;

import fr.croixrouge.config.JwtTokenConfig;
import fr.croixrouge.domain.model.User;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.exception.UserNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private JwtTokenConfig jwtTokenConfig;

    @Autowired
    private SecretKey secretKey;

    public AuthenticationService(UserRepository userRepository, JwtTokenConfig jwtTokenConfig, SecretKey secretKey) {
        this.userRepository = userRepository;
        this.jwtTokenConfig = jwtTokenConfig;
        this.secretKey = secretKey;
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User id not found: " + userId));
    }

    public String getUserIdFromJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(jwtTokenConfig.getTokenHeader());

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authorizationHeader.substring("Bearer ".length());

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);

        return claimsJws.getBody().get("userId", String.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}

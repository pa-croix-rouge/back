package fr.croixrouge.service;

import fr.croixrouge.config.JwtTokenConfig;
import fr.croixrouge.domain.model.Entity;
import fr.croixrouge.domain.repository.BeneficiaryRepository;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.UserRepository;
import fr.croixrouge.domain.repository.VolunteerRepository;
import fr.croixrouge.exposition.dto.core.LoginResponse;
import fr.croixrouge.model.UserSecurity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    //    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenConfig jwtTokenConfig;
    private final AuthenticationManager authenticationManager;
    private final VolunteerRepository volunteerRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenConfig jwtTokenConfig, AuthenticationManager authenticationManager, VolunteerRepository volunteerRepository, BeneficiaryRepository beneficiaryRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenConfig = jwtTokenConfig;
        this.authenticationManager = authenticationManager;
        this.volunteerRepository = volunteerRepository;
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public void register() {
       /* var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();*/
    }

    public LoginResponse authenticateVolunteer(String userName, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userName,
                        password
                )
        );

        var user = userRepository.findByUsername(userName).map(UserSecurity::new)
                .orElseThrow();

        if (volunteerRepository.findByUsername(user.getUsername()).isEmpty()) {
            throw new UsernameNotFoundException("User " + user.getUsername() + " is not a volunteer.");
        }
        var jwtToken = jwtTokenConfig.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return new LoginResponse(jwtToken);
    }

    public LoginResponse authenticateBeneficiary(String userName, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userName,
                        password
                )
        );

        var user = userRepository.findByUsername(userName).map(UserSecurity::new)
                .orElseThrow();
        if (beneficiaryRepository.findByUsername(user.getUsername()).isEmpty()) {
            throw new UsernameNotFoundException("User " + user.getUsername() + " is not a beneficiary.");
        }
        var jwtToken = jwtTokenConfig.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return new LoginResponse(jwtToken);
    }

    //todo : implement
    private void saveUserToken(UserSecurity user, String jwtToken) {
//        var token = Token.builder()
//                .user(user)
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//        tokenRepository.save(token);
    }

    //todo : implement
    private void revokeAllUserTokens(UserSecurity user) {
//        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//        if (validUserTokens.isEmpty())
//            return;
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//        tokenRepository.saveAll(validUserTokens);
    }

    public String getUsernameFromJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(jwtTokenConfig.getTokenHeader());

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authorizationHeader.substring("Bearer ".length());

        return this.jwtTokenConfig.extractUsername(token);
    }

    public ID getUserIdFromJwtToken(HttpServletRequest request) {
        String username = this.getUsernameFromJwtToken(request);
        if (username == null) {
            return null;
        }

        return this.userRepository.findByUsername(username).map(Entity::getId).orElse(null);
    }

    public ID getUserLocalUnitIdFromJwtToken(HttpServletRequest request) {
        String username = this.getUsernameFromJwtToken(request);
        if (username == null) {
            return null;
        }

        return this.userRepository.findByUsername(username).map(user -> user.getLocalUnit().getId()).orElse(null);
    }
}

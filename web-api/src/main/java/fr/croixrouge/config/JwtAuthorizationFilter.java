package fr.croixrouge.config;

import fr.croixrouge.service.UserSecurityService;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserSecurityService userSecurityService;
    private final JwtTokenConfig jwtTokenConfig;

    public JwtAuthorizationFilter(UserSecurityService userSecurityService, JwtTokenConfig jwtTokenConfig) {
        this.userSecurityService = userSecurityService;
        this.jwtTokenConfig = jwtTokenConfig;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(jwtTokenConfig.getTokenHeader());
        final String token;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith(jwtTokenConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        token = authHeader.substring(jwtTokenConfig.getTokenPrefix().length());

        try {
            userEmail = jwtTokenConfig.extractUsername(token);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userSecurityService.loadUserByUsername(userEmail);

                if (jwtTokenConfig.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        filterChain.doFilter(request, response);
    }

}

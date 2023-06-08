package fr.croixrouge.config;

import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.model.UserSecurity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class OperationFilter extends OncePerRequestFilter {

    private final RequestMatcher customFilterUrl;

    private final List<RequestMatcher> excludedFilterUrl;

    private final Operations operation;

    public OperationFilter(RequestMatcher customFilterUrl, List<RequestMatcher> excludedFilterUrl, Operations operation) {
        this.customFilterUrl = customFilterUrl;
        this.excludedFilterUrl = excludedFilterUrl;

        this.operation = operation;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !customFilterUrl.matches(request) || excludedFilterUrl.stream().anyMatch(matcher -> matcher.matches(request));
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        UserSecurity userSecurity = (UserSecurity) auth.getPrincipal();

        userSecurity.currentRoles = userSecurity.currentRoles.stream()
                .filter(role -> role.getAuthorizations().entrySet().stream()
                        .anyMatch(entry -> entry.getValue().contains(operation)) )
                .toList();

        if (userSecurity.currentRoles.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

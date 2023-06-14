package fr.croixrouge.config;

import fr.croixrouge.domain.model.Resources;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RessourceFilter extends OncePerRequestFilter {

    private final RequestMatcher customFilterUrl;

    private final List<RequestMatcher> excludedFilterUrl;

    private final Resources resource;

    public RessourceFilter(RequestMatcher customFilterUrl, List<RequestMatcher> excludedFilterUrl, Resources resource) {
        this.customFilterUrl = customFilterUrl;
        this.excludedFilterUrl = excludedFilterUrl;
        this.resource = resource;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !customFilterUrl.matches(request) || excludedFilterUrl.stream().anyMatch(matcher -> matcher.matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        UserSecurity userSecurity = (UserSecurity) auth.getPrincipal();

        userSecurity.allAuthorizations = userSecurity.allAuthorizations.entrySet().stream()
                .filter(entry -> entry.getKey().equals(resource))
                .map( entry -> Map.entry( resource, entry.getValue()))
                .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ));

        if (userSecurity.allAuthorizations.isEmpty()) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }


        filterChain.doFilter(request, response);
    }
}

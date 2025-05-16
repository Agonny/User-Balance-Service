package com.example.userBalanceApp.security;

import com.example.userBalanceApp.exception.MissingTokenException;
import com.example.userBalanceApp.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final List<RequestMatcher> paths = List.of(AntPathRequestMatcher.antMatcher("/api/v1/auth/authenticate"),
            AntPathRequestMatcher.antMatcher("/swagger-ui.html"), AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
            AntPathRequestMatcher.antMatcher("/swagger-resources/**"), AntPathRequestMatcher.antMatcher("/v3/api-docs"),
            AntPathRequestMatcher.antMatcher("/v3/api-docs/**"));

    private final AuthService authService;

    private final OrRequestMatcher matcher = new OrRequestMatcher(paths);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!isPermittedPath(request)) {
            String token = request.getHeader(AUTHORIZATION_HEADER);

            if(token == null || token.substring(BEARER_PREFIX.length()).isEmpty()) throw new MissingTokenException();

            authService.authorize(request, response, token.substring(BEARER_PREFIX.length()));
        }

        filterChain.doFilter(request, response);
    }

    protected boolean isPermittedPath(HttpServletRequest request) {
        return matcher.matches(request);
    }

}

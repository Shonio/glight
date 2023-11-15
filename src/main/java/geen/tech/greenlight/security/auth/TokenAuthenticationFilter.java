package geen.tech.greenlight.security.auth;

import geen.tech.greenlight.model.user.User;
import geen.tech.greenlight.security.TokenHelper;
import geen.tech.greenlight.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private TokenHelper tokenHelper;
    private CustomUserDetailsService customUserDetailsService;

    public TokenAuthenticationFilter(TokenHelper tokenHelper, CustomUserDetailsService customUserDetailsService) {
        this.tokenHelper = tokenHelper;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = tokenHelper.getToken(request);
        System.out.println("authToken: " + authToken);

        if (authToken != null) {
            // get username from token
            String username = tokenHelper.getUsernameFromToken(authToken);
            System.out.println("username: " + username);
            if (username != null) {
                User user = customUserDetailsService.loadUserByUsername(username);
                if (tokenHelper.validateToken(authToken, user)) {
                    // create authentication
                    TokenBasedAuthentication authentication = new TokenBasedAuthentication(user);
                    authentication.setToken(authToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
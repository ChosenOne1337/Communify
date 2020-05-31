package ru.nsu.ccfit.mvcentertainment.communify.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.exceptions.JwtValidationException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public JwtRequestFilter(
            JwtTokenUtils jwtTokenUtils,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String jwtToken = resolveToken(httpServletRequest);
        try {
            if (jwtToken != null) {
                Authentication authentication = createAuthenticationFromJwtToken(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtValidationException e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerPrefix = "Bearer ";
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith(bearerPrefix)) {
            return null;
        }

        return bearerToken.substring(bearerPrefix.length());
    }

    private Authentication createAuthenticationFromJwtToken(String jwtToken) {
        JwtTokenInfo jwtTokenInfo = jwtTokenUtils.parseToken(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenInfo.getUserName());
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", Collections.emptyList()
        );
    }
}

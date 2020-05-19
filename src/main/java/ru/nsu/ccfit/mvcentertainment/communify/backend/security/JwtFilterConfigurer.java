package ru.nsu.ccfit.mvcentertainment.communify.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenUtils jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JwtFilterConfigurer(
            JwtTokenUtils jwtTokenProvider,
            UserDetailsServiceImpl userDetailsService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(HttpSecurity http) {
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtTokenProvider, userDetailsService);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}

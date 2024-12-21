package com.company.logistics.config;

import com.company.logistics.utils.JwtUtil;
import com.company.logistics.utils.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthemticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final UserDetailsService userEmployeeDetailsService;

    public JwtAuthemticationFilter(@Qualifier("userDetailsService") UserDetailsService service, JwtUtil jwtUtil, @Qualifier("userEmployeeDetailsService") UserDetailsService service2){
        this.jwtUtil = jwtUtil;
        this.userDetailsService = service;
        this.userEmployeeDetailsService = service2;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String name;
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        name = jwtUtil.extractUsername(jwt);
        String role = jwtUtil.extractClaim(jwt, claims -> claims.get("role", String.class));
        if(role.equals(Role.COMPANY.name())){
            if(name != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(name);
                if (jwtUtil.isTokenValid(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        else if(role.equals(Role.EMPLOYEE.name())){
            if(name != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.userEmployeeDetailsService.loadUserByUsername(name);
                if (jwtUtil.isTokenValid(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

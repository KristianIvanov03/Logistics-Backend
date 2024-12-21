package com.company.logistics.config;

import com.company.logistics.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthemticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private final AuthenticationProvider employeeAuthenticationProvider;

    private final JwtEmployeeAuthenticationFilter jwtEmployeeAuthenticationFilter;

    public SecurityConfig(@Qualifier("authenticationProvider") AuthenticationProvider provider, JwtAuthemticationFilter filter, @Qualifier("employeeAuthenticationProvider") AuthenticationProvider provider2, JwtEmployeeAuthenticationFilter filter2){
        this.authenticationProvider = provider;
        this.jwtAuthFilter = filter;
        this.employeeAuthenticationProvider = provider2;
        this.jwtEmployeeAuthenticationFilter = filter2;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf()
                .disable()
                .securityMatcher("/api/companies/**")
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/companies/account/register", "/api/companies/account/login").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public SecurityFilterChain securityEmployeeFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf()
                .disable()
                .securityMatcher("/api/employee/**")
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/employee/account/auth/register", "/api/employee/account/auth/login").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(employeeAuthenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}


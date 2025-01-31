package com.company.logistics.config;

import com.company.logistics.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthemticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private final AuthenticationProvider employeeAuthenticationProvider;

    private final AuthenticationProvider clientsAuthenticationProvider;

    public SecurityConfig(@Qualifier("authenticationProvider") AuthenticationProvider provider,
                          JwtAuthemticationFilter filter,
                          @Qualifier("employeeAuthenticationProvider") AuthenticationProvider provider2,
                          @Qualifier("clientAuthenticationProvider") AuthenticationProvider provider3){
        this.authenticationProvider = provider;
        this.jwtAuthFilter = filter;
        this.employeeAuthenticationProvider = provider2;
        this.clientsAuthenticationProvider = provider3;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf()
                .disable()
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .securityMatcher("/api/companies/**")
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/companies/account/register", "/api/companies/account/login", "/api/companies/account//get-all-companies").permitAll();
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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

    @Bean
    public SecurityFilterChain securityClientFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf()
                .disable()
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .securityMatcher("/api/clients/**")
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/clients/account/auth/register", "/api/clients/account/auth/login").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(clientsAuthenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
    // CORS configuration setup
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "https://example.com")); // Frontend URLs
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // HTTP methods
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "X-Requested-With", "Accept", "Origin")); // Allowed headers
        configuration.setAllowCredentials(true); // Allow cookies and credentials

        // Registering the CORS configuration for all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}


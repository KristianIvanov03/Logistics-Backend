package com.company.logistics.config;

import com.company.logistics.repository.ClientAccountRepository;
import com.company.logistics.repository.CompanyRepository;
import com.company.logistics.repository.EmployeeAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final CompanyRepository companyRepository;
    private final EmployeeAccountRepository employeeAccountRepository;
    private final ClientAccountRepository clientAccountRepository;
    @Bean
    @Qualifier("userDetailsService")
    public UserDetailsService userDetailsService(){
        return username -> companyRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    @Bean
    @Qualifier("userEmployeeDetailsService")
    public UserDetailsService userEmployeeDetailsService(){
        return username -> employeeAccountRepository.findByName(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    @Bean
    @Qualifier("userClientDetailsService")
    public UserDetailsService userClientDetailsService(){
        return username -> clientAccountRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    @Qualifier("authenticationProvider")
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    @Qualifier("employeeAuthenticationProvider")
    public AuthenticationProvider employeeAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userEmployeeDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    @Qualifier("clientAuthenticationProvider")
    public AuthenticationProvider clientAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userClientDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Qualifier("authenticationManager")
    public AuthenticationManager authenticationManager() throws Exception{
        return new ProviderManager(List.of(authenticationProvider(), employeeAuthenticationProvider(), clientAuthenticationProvider()));
    }
}

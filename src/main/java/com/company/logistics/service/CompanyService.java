package com.company.logistics.service;

import com.company.logistics.exception.ResourceNotFoundException;
import com.company.logistics.model.company.*;
import com.company.logistics.repository.CompanyRepository;
import com.company.logistics.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerCompany(RegisterRequest registerRequest){
        var company1 = Company.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .address(registerRequest.getAddress())
                .taxNumber(registerRequest.getTaxNumber())
                .role(registerRequest.getRole())
                .phoneNumber(registerRequest.getPhoneNumber())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        companyRepository.save(company1);
        var jwtToken = jwtUtil.generateToken(company1);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public AuthenticationResponse loginCompany(AuthenticationRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getName(),
                        loginRequest.getPassword()
                )
        );
        var user = companyRepository.findByName(loginRequest.getName()).orElseThrow();
        var authToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder().token(authToken).build();
    }

    public void resetPassword(PasswordResetRequest request){
        Company company = companyRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Company not Found"));
        company.setPassword(passwordEncoder.encode(request.getNewPassword()));
        companyRepository.save(company);
    }
}

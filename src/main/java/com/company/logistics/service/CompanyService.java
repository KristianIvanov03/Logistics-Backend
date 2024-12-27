package com.company.logistics.service;

import com.company.logistics.exception.ResourceNotFoundException;
import com.company.logistics.model.company.*;
import com.company.logistics.repository.CompanyRepository;
import com.company.logistics.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", registerRequest.getRole());
        var jwtToken = jwtUtil.generateToken(extraClaims, company1);
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
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        var authToken = jwtUtil.generateToken(extraClaims, user);
        return AuthenticationResponse.builder().token(authToken).build();
    }

    public void resetPassword(PasswordResetRequest request){
        Company company = companyRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Company not Found"));
        company.setPassword(passwordEncoder.encode(request.getNewPassword()));
        companyRepository.save(company);
    }

    public UpdateCompanyResponse updateCompanyInfo(UpdateCompanyRequest request){
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Company company = companyRepository.findByName(authenticatedUsername).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        if(request.getEmail() != null){
            company.setEmail(request.getEmail());
        }
        if(request.getAddress() != null){
            company.setAddress(request.getAddress());
        }
        if(request.getPhoneNumber() != null){
            company.setPhoneNumber(request.getPhoneNumber());
        }
        Company companyUpdated = companyRepository.save(company);
        return UpdateCompanyResponse.builder()
                .address(companyUpdated.getAddress())
                .phoneNumber(companyUpdated.getPhoneNumber())
                .email(companyUpdated.getEmail())
                .build();
    }

    public CompanyInfoResponse getCompanyInfo(){
        String authenticationUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Company company = companyRepository.findByName(authenticationUserName).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return CompanyInfoResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .address(company.getAddress())
                .email(company.getEmail())
                .phoneNumber(company.getPhoneNumber())
                .role(company.getRole())
                .build();
    }
}

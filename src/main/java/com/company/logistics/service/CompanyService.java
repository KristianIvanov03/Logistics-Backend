package com.company.logistics.service;

import com.company.logistics.exception.AuthorizationException;
import com.company.logistics.model.company.*;
import com.company.logistics.model.entities.Company;
import com.company.logistics.repository.CompanyRepository;
import com.company.logistics.utils.AuthenticationService;
import com.company.logistics.utils.GlobalMapper;
import com.company.logistics.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;

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
        Company companyreg =companyRepository.save(company1);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", registerRequest.getRole());
        extraClaims.put("id", companyreg.getId());
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
        var user = companyRepository.findByName(loginRequest.getName()).orElseThrow(() -> new AuthorizationException("Company account not found for this email address"));
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        var authToken = jwtUtil.generateToken(extraClaims, user);
        return AuthenticationResponse.builder().token(authToken).build();
    }

    public void resetPassword(PasswordResetRequest request){
        Company company = authenticationService.getAuthenticatedCompany();
        company.setPassword(passwordEncoder.encode(request.getNewPassword()));
        companyRepository.save(company);
    }

    public UpdateCompanyResponse updateCompanyInfo(UpdateCompanyRequest request){
        Company company = authenticationService.getAuthenticatedCompany();
        Optional.ofNullable(request.getEmail()).ifPresent(company::setEmail);
        Optional.ofNullable(request.getAddress()).ifPresent(company::setAddress);
        Optional.ofNullable(request.getPhoneNumber()).ifPresent(company::setPhoneNumber);
        Company companyUpdated = companyRepository.save(company);
        return UpdateCompanyResponse.builder()
                .address(companyUpdated.getAddress())
                .phoneNumber(companyUpdated.getPhoneNumber())
                .email(companyUpdated.getEmail())
                .build();
    }

    public CompanyInfoResponse getCompanyInfo(){
        Company company = authenticationService.getAuthenticatedCompany();
        return GlobalMapper.toCompanyInfo(company);
    }

    public List<CompanyNamesAndIdsDTO> getAllCompanies(){
        return companyRepository.findAll().stream()
                .map(GlobalMapper::buildCompanyNameAndId)
                .collect(Collectors.toList());
    }
}

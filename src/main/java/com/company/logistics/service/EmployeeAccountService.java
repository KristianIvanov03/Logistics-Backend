package com.company.logistics.service;

import com.company.logistics.model.company.AuthenticationRequest;
import com.company.logistics.model.company.AuthenticationResponse;
import com.company.logistics.model.company.Company;
import com.company.logistics.model.employeeaccaunts.EmployeeAccount;
import com.company.logistics.model.employeeaccaunts.EmployeeRegisterRequest;
import com.company.logistics.model.office.Office;
import com.company.logistics.repository.CompanyRepository;
import com.company.logistics.repository.EmployeeAccountRepository;
import com.company.logistics.repository.OfficeRepository;
import com.company.logistics.utils.EmployeeRole;
import com.company.logistics.utils.JwtUtil;
import com.company.logistics.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeAccountService {
    private final EmployeeAccountRepository employeeAccountRepository;
    private final OfficeRepository officeRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthenticationResponse registerEmployeeAccount(EmployeeRegisterRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        Office office = officeRepository.findById(request.getOfficeId())
                .orElseThrow(() -> new UsernameNotFoundException("Office not found"));
        EmployeeAccount account = EmployeeAccount.builder()
                .name(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .lastName(request.getLastName())
                .employeeRole(request.getEmployeeRole())
                .role(request.getRole())
                .egn(request.getEgn())
                .office(office)
                .company(company).build();

        employeeAccountRepository.save(account);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", request.getRole());
        var jwtToken = jwtUtil.generateToken(extraClaims,account);
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
        var user = employeeAccountRepository.findByName(loginRequest.getName()).orElseThrow();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        var authToken = jwtUtil.generateToken(extraClaims, user);
        return AuthenticationResponse.builder().token(authToken).build();
    }
}


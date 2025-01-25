package com.company.logistics.utils;


import com.company.logistics.exception.AuthorizationException;
import com.company.logistics.model.entities.ClientAccount;
import com.company.logistics.model.entities.Company;
import com.company.logistics.model.entities.EmployeeAccount;
import com.company.logistics.repository.ClientAccountRepository;
import com.company.logistics.repository.CompanyRepository;
import com.company.logistics.repository.EmployeeAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CompanyRepository companyRepository;
    private final EmployeeAccountRepository employeeAccountRepository;
    private final ClientAccountRepository clientAccountRepository;

    public Company getAuthenticatedCompany() {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return companyRepository.findByName(authenticatedUsername).orElseThrow(() -> new AuthorizationException("Company not found"));
    }

    public EmployeeAccount getAuthenticatedEmployee() {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return employeeAccountRepository.findByName(authenticatedUsername).orElseThrow(() -> new AuthorizationException("Employee not found"));
    }

    public ClientAccount getAuthenticatedClient(){
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return clientAccountRepository.findByName(authenticatedUsername).orElseThrow(() -> new AuthorizationException("Company not found"));
    }
}

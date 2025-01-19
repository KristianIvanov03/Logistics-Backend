package com.company.logistics.utils;


import com.company.logistics.exception.AuthorizationException;
import com.company.logistics.model.entities.Company;
import com.company.logistics.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CompanyRepository companyRepository;

    public Company getAuthenticatedCompany() {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return companyRepository.findByName(authenticatedUsername).orElseThrow(() -> new AuthorizationException("Company not found"));
    }
}

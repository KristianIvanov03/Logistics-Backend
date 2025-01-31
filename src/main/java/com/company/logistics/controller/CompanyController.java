package com.company.logistics.controller;

import com.company.logistics.model.company.*;
import com.company.logistics.service.CompanyService;
import com.company.logistics.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies/account")
@RequiredArgsConstructor
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerCompany(@RequestBody @Valid RegisterRequest company){
        return ResponseEntity.ok(companyService.registerCompany(company));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginCompany(@RequestBody @Valid AuthenticationRequest loginRequest){
        return ResponseEntity.ok(companyService.loginCompany(loginRequest));
    }

    @PreAuthorize("hasRole('COMPANY')")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetRequest request){
        companyService.resetPassword(request);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PreAuthorize("hasRole('COMPANY')")
    @PutMapping("/update")
    public ResponseEntity<UpdateCompanyResponse> updateCompanyInfo(@RequestBody UpdateCompanyRequest request){
        UpdateCompanyResponse updatedCompany = companyService.updateCompanyInfo(request);
        return ResponseEntity.ok(updatedCompany);
    }

    @PreAuthorize("hasRole('COMPANY')")
    @GetMapping("/info")
    public ResponseEntity<CompanyInfoResponse> getCompanyInfo(){
        return ResponseEntity.ok(companyService.getCompanyInfo());
    }

    @GetMapping("/get-all-companies")
    public ResponseEntity<List<CompanyNamesAndIdsDTO>> getAllCompanies(){
        return ResponseEntity.ok(companyService.getAllCompanies());
    }
}

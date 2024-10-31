package com.company.logistics.controller;

import com.company.logistics.model.company.AuthenticationRequest;
import com.company.logistics.model.company.AuthenticationResponse;
import com.company.logistics.model.company.Company;
import com.company.logistics.model.company.RegisterRequest;
import com.company.logistics.service.CompanyService;
import com.company.logistics.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerCompany(@RequestBody RegisterRequest company){
        return ResponseEntity.ok(companyService.registerCompany(company));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginCompany(@RequestBody AuthenticationRequest loginRequest){
        return ResponseEntity.ok(companyService.loginCompany(loginRequest));
    }
}

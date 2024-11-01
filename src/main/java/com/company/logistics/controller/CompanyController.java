package com.company.logistics.controller;

import com.company.logistics.model.company.*;
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

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request){
        companyService.resetPassword(request);
        return ResponseEntity.ok("Password updated successfully");
    }
}

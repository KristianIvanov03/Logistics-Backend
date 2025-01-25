package com.company.logistics.controller;

import com.company.logistics.model.company.AuthenticationRequest;
import com.company.logistics.model.company.AuthenticationResponse;
import com.company.logistics.model.company.PasswordResetRequest;
import com.company.logistics.model.employeeaccaunts.EmployeeRegisterRequest;
import com.company.logistics.service.EmployeeAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee/account")
@RequiredArgsConstructor
public class EmployeeAccountController {
    private final EmployeeAccountService employeeAccountService;

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> loginCompany(@RequestBody @Valid AuthenticationRequest loginRequest){
        return ResponseEntity.ok(employeeAccountService.loginCompany(loginRequest));
    }

    @PostMapping("/auth/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid PasswordResetRequest request){
        employeeAccountService.changePassword(request);
        return ResponseEntity.ok("Password updated successfully");
    }
}

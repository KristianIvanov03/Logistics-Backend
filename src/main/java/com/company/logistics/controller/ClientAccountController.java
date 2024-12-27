package com.company.logistics.controller;

import com.company.logistics.model.clientaccount.ClientRegisterRequest;
import com.company.logistics.model.company.AuthenticationRequest;
import com.company.logistics.model.company.AuthenticationResponse;
import com.company.logistics.model.employeeaccaunts.EmployeeRegisterRequest;
import com.company.logistics.service.ClientAccountService;
import com.company.logistics.service.EmployeeAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients/account")
@RequiredArgsConstructor
public class ClientAccountController {
    private final ClientAccountService clientAccountService;

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> registerEmployee(@RequestBody ClientRegisterRequest request){
        return ResponseEntity.ok(clientAccountService.registerEmployeeAccount(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> loginCompany(@RequestBody AuthenticationRequest loginRequest) {
        return ResponseEntity.ok(clientAccountService.loginCompany(loginRequest));
    }
}

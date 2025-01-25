package com.company.logistics.controller;

import com.company.logistics.model.clientaccount.ClientRegisterRequest;
import com.company.logistics.model.clientaccount.ClientResponseDTO;
import com.company.logistics.model.company.AuthenticationRequest;
import com.company.logistics.model.company.AuthenticationResponse;
import com.company.logistics.model.employeeaccaunts.EmployeeRegisterRequest;
import com.company.logistics.service.ClientAccountService;
import com.company.logistics.service.EmployeeAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients/account")
@RequiredArgsConstructor
public class ClientAccountController {
    private final ClientAccountService clientAccountService;

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> registerEmployee(@RequestBody @Valid ClientRegisterRequest request){
        return ResponseEntity.ok(clientAccountService.registerEmployeeAccount(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> loginCompany(@RequestBody @Valid AuthenticationRequest loginRequest) {
        return ResponseEntity.ok(clientAccountService.loginCompany(loginRequest));
    }

    @GetMapping
    public ResponseEntity<ClientResponseDTO> getClientInfo(){
        return ResponseEntity.ok(clientAccountService.getClientInfo());
    }
}

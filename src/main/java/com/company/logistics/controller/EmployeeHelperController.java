package com.company.logistics.controller;

import com.company.logistics.model.clientaccount.ClientResponseDTO;
import com.company.logistics.service.EmployeeAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeHelperController {

    private final EmployeeAccountService employeeAccountService;

    @GetMapping("/get-all-clients")
    public ResponseEntity<List<ClientResponseDTO>> getAllClients(){
        return ResponseEntity.ok(employeeAccountService.getAllClients());
    }
}

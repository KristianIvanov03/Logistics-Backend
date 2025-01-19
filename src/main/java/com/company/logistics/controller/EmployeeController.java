package com.company.logistics.controller;

import com.company.logistics.model.employee.EmployeeRequestDTO;
import com.company.logistics.model.employee.EmployeeResponseDTO;
import com.company.logistics.model.employeeaccaunts.EmployeeRegisterRequest;
import com.company.logistics.model.employeeaccaunts.EmployeeRegisterResponse;
import com.company.logistics.model.employeeaccaunts.UpdateEmployeeRequest;
import com.company.logistics.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PreAuthorize("hasRole('COMPANY')")
    @PostMapping("/add")
    public ResponseEntity<EmployeeRegisterResponse> addEmployee(@RequestBody EmployeeRegisterRequest requestDTO){
        return ResponseEntity.ok(employeeService.addEmployee(requestDTO));
    }

    @PreAuthorize("hasRole('COMPANY')")
    @PutMapping("/update")
    public ResponseEntity<EmployeeRegisterResponse> updateEmployee(@RequestBody UpdateEmployeeRequest requestDTO){
        return ResponseEntity.ok(employeeService.updateEmployee(requestDTO));
    }

    @PreAuthorize("hasRole('COMPANY')")
    @GetMapping
    public ResponseEntity<List<EmployeeRegisterResponse>> getEmployees(){
        return ResponseEntity.ok(employeeService.getEmployeesForCompany());
    }

    @PreAuthorize("hasRole('COMPANY')")
    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId){
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee Removed Successfully");
    }
}

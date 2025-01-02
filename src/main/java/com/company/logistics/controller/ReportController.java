package com.company.logistics.controller;

import com.company.logistics.model.RevenueInPeriodRequest;
import com.company.logistics.model.employee.EmployeeResponseDTO;
import com.company.logistics.model.entities.Package;
import com.company.logistics.model.packages.ClientInfo;
import com.company.logistics.model.packages.PackageResonseDto;
import com.company.logistics.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/companies/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployees(){
        return ResponseEntity.ok(reportService.getAllEmployees());
    }
    @GetMapping("/clients")
    public ResponseEntity<List<ClientInfo>> getClients(){
        return ResponseEntity.ok(reportService.getAllClients());
    }
    @GetMapping("/packages")
    public ResponseEntity<List<PackageResonseDto>> getPackages(){
        return ResponseEntity.ok(reportService.getAllPackages());
    }
    @GetMapping("/packagesByEmployee/{id}")
    public ResponseEntity<List<PackageResonseDto>> getAllPackagesByRegisteredEmployee(@PathVariable Long id){
        return ResponseEntity.ok(reportService.getAllPackagesByRegisteredEmployee(id));
    }
    @GetMapping("/pendingPackages")
    public ResponseEntity<List<PackageResonseDto>> getAllPendingPackages(){
        return ResponseEntity.ok(reportService.getAllPendingPackages());
    }
    @PostMapping("/revenueInPeriod")
    public ResponseEntity<BigDecimal> getRevenueInPeriod(@RequestBody RevenueInPeriodRequest request){
        return ResponseEntity.ok(reportService.getRevenueInPeriod(request));
    }
    @GetMapping("/clientSentPackages/{id}")
    public ResponseEntity<List<PackageResonseDto>> getAllSentPackagesByClient(@PathVariable Long id){
        return ResponseEntity.ok(reportService.getAllSentPackages(id));
    }
    @GetMapping("/clientReceivedPackages/{id}")
    public ResponseEntity<List<PackageResonseDto>> getAllReceivedPackagesByClient(@PathVariable Long id){
        return ResponseEntity.ok(reportService.getAllReceivedPackages(id));
    }
}

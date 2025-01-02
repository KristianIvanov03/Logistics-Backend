package com.company.logistics.controller;

import com.company.logistics.model.entities.Package;
import com.company.logistics.model.packages.PackageRequestDto;
import com.company.logistics.model.packages.PackageResonseDto;
import com.company.logistics.model.packages.UpdateStatusRequest;
import com.company.logistics.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee/packages")
@RequiredArgsConstructor
public class PackageController {
    private final PackageService packageService;
    @PostMapping
    public ResponseEntity<PackageResonseDto> makePackage(@RequestBody PackageRequestDto request){
        return ResponseEntity.ok(packageService.createPackage(request));
    }
    @PostMapping("/updateStatus")
    public ResponseEntity<PackageResonseDto> updateStatus(@RequestBody UpdateStatusRequest updateStatusRequest){
        return ResponseEntity.ok(packageService.updateStatus(updateStatusRequest));
    }
    @PostMapping("/finishPackage/{id}")
    public ResponseEntity<PackageResonseDto> finishPackage(@PathVariable Long id){
        return ResponseEntity.ok(packageService.finishPackage(id));
    }
}

package com.company.logistics.controller;

import com.company.logistics.model.office.OfficeRequestDTO;
import com.company.logistics.model.office.OfficeResponseDTO;
import com.company.logistics.service.OfficeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/companies/offices")
@RequiredArgsConstructor
public class OfficeController {
    private final OfficeService officeService;


    @PostMapping("/add")
    public ResponseEntity<OfficeResponseDTO> addOffice(@RequestBody @Valid OfficeRequestDTO requestDTO){
        return ResponseEntity.ok(officeService.addOffice(requestDTO));
    }

    @PutMapping("/update/{officeId}")
    public ResponseEntity<OfficeResponseDTO> updateOffice(@PathVariable Long officeId, @RequestBody OfficeRequestDTO requestDTO){
            OfficeResponseDTO response = officeService.updateOffice(officeId, requestDTO);
            return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('COMPANY')")
    @GetMapping
    public ResponseEntity<List<OfficeResponseDTO>> getOffices(){
        return ResponseEntity.ok(officeService.getOfficesForCompany());
    }

    @PreAuthorize("hasRole('COMPANY')")
    @DeleteMapping("/delete/{officeId}")
    public ResponseEntity<String> deleteOffice(@PathVariable Long officeId){
        try{
            officeService.deleteOffice(officeId);
            return ResponseEntity.ok("Office deleted sucessfully");
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}

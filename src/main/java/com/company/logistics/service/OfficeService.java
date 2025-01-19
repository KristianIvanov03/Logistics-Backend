package com.company.logistics.service;

import com.company.logistics.model.entities.Company;
import com.company.logistics.model.entities.Office;
import com.company.logistics.model.office.OfficeRequestDTO;
import com.company.logistics.model.office.OfficeResponseDTO;
import com.company.logistics.repository.CompanyRepository;
import com.company.logistics.repository.OfficeRepository;
import com.company.logistics.utils.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfficeService {
    private final OfficeRepository officeRepository;
    private final AuthenticationService authenticationService;

    @Transactional
    public OfficeResponseDTO addOffice(OfficeRequestDTO requestDTO){
        Company company = authenticationService.getAuthenticatedCompany();

        Office office = Office.builder()
                .address(requestDTO.getAddress())
                .phoneNumber(requestDTO.getPhoneNumber())
                .company(company).build();


        Office savedOffice = officeRepository.save(office);
        return mapToResponseDTO(savedOffice);
    }

    @Transactional
    public OfficeResponseDTO updateOffice(Long officeId, OfficeRequestDTO requestDTO) throws AccessDeniedException{
        Office office = officeRepository.findById(officeId).orElseThrow(() -> new EntityNotFoundException("Office not found"));
        Company company = authenticationService.getAuthenticatedCompany();

        if(!office.getCompany().getId().equals(company.getId())){
            throw new AccessDeniedException("You do not have permission to update this office");
        }

        Optional.ofNullable(requestDTO.getAddress()).ifPresent(office::setAddress);
        Optional.ofNullable(requestDTO.getPhoneNumber()).ifPresent(office::setPhoneNumber);

        Office savedOffice = officeRepository.save(office);
        return mapToResponseDTO(savedOffice);
    }

    @Transactional(readOnly = true)
    public List<OfficeResponseDTO> getOfficesForCompany(){
        Company company = authenticationService.getAuthenticatedCompany();
        List<Office> offices = officeRepository.findByCompany(company);
        List<OfficeResponseDTO> response = offices.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
        return response;
    }

    @Transactional
    public void deleteOffice(Long officeId) throws AccessDeniedException{
        Office office = officeRepository.findById(officeId)
                .orElseThrow(() -> new EntityNotFoundException("Office not found"));

        Company company = authenticationService.getAuthenticatedCompany();

        // Check if the office belongs to the authenticated company
        if (!office.getCompany().getId().equals(company.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this office");
        }

        officeRepository.delete(office);
    }

    private OfficeResponseDTO mapToResponseDTO(Office office){
        return new OfficeResponseDTO(
                office.getId(),
                office.getAddress(),
                office.getPhoneNumber(),
                office.getCompany().getId(),
                null
        );
    }
}

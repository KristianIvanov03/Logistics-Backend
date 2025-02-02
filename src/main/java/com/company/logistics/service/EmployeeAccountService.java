package com.company.logistics.service;

import com.company.logistics.model.company.AuthenticationRequest;
import com.company.logistics.model.company.AuthenticationResponse;
import com.company.logistics.model.company.PasswordResetRequest;
import com.company.logistics.model.employeeaccaunts.EmployeeRegisterResponse;
import com.company.logistics.model.entities.Company;
import com.company.logistics.model.entities.EmployeeAccount;
import com.company.logistics.model.employeeaccaunts.EmployeeRegisterRequest;
import com.company.logistics.model.entities.Office;
import com.company.logistics.model.office.OfficeResponseDTO;
import com.company.logistics.repository.CompanyRepository;
import com.company.logistics.repository.EmployeeAccountRepository;
import com.company.logistics.repository.OfficeRepository;
import com.company.logistics.utils.AuthenticationService;
import com.company.logistics.utils.GlobalMapper;
import com.company.logistics.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeAccountService {
    private final EmployeeAccountRepository employeeAccountRepository;
    private final OfficeRepository officeRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthenticationService authenticationService;

    public EmployeeRegisterResponse registerEmployeeAccount(EmployeeRegisterRequest request) {
        Company company = authenticationService.getAuthenticatedCompany();
        Office office = officeRepository.findById(request.getOfficeId())
                .orElseThrow(() -> new UsernameNotFoundException("Office not found"));
        EmployeeAccount account = EmployeeAccount.builder()
                .name(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .lastName(request.getLastName())
                .employeeRole(request.getEmployeeRole())
                .role(request.getRole())
                .egn(request.getEgn())
                .office(office)
                .company(company).build();

        EmployeeAccount account1 = employeeAccountRepository.save(account);
        return GlobalMapper.buildEmployeeResponse(account1);
    }

    public AuthenticationResponse loginCompany(AuthenticationRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getName(),
                        loginRequest.getPassword()
                )
        );
        var user = employeeAccountRepository.findByName(loginRequest.getName()).orElseThrow();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        extraClaims.put("id", user.getId());
        var authToken = jwtUtil.generateToken(extraClaims, user);
        return AuthenticationResponse.builder().token(authToken).build();
    }

    public void changePassword(PasswordResetRequest request){
        EmployeeAccount account = authenticationService.getAuthenticatedEmployee();
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        employeeAccountRepository.save(account);
    }

    public void deleteEmployee(Long id) throws AccessDeniedException{
        Company company = authenticationService.getAuthenticatedCompany();
        EmployeeAccount employeeAccount = employeeAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Office not found"));
        // Check if the office belongs to the authenticated company
        if (!employeeAccount.getCompany().getId().equals(company.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this office");
        }

        employeeAccountRepository.delete(employeeAccount);

    }
}


package com.company.logistics.service;

import com.company.logistics.exception.AuthorizationException;
import com.company.logistics.model.employeeaccaunts.EmployeeRegisterRequest;
import com.company.logistics.model.employeeaccaunts.EmployeeRegisterResponse;
import com.company.logistics.model.employeeaccaunts.UpdateEmployeeRequest;
import com.company.logistics.model.entities.Company;
import com.company.logistics.model.entities.Employee;
import com.company.logistics.model.employee.EmployeeRequestDTO;
import com.company.logistics.model.employee.EmployeeResponseDTO;
import com.company.logistics.model.entities.EmployeeAccount;
import com.company.logistics.model.entities.Office;
import com.company.logistics.model.office.OfficeResponseDTO;
import com.company.logistics.repository.CompanyRepository;
import com.company.logistics.repository.EmployeeAccountRepository;
import com.company.logistics.repository.EmployeeRepository;
import com.company.logistics.utils.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AuthenticationService authenticationService;
    private final EmployeeAccountService employeeAccountService;
    private final EmployeeAccountRepository employeeAccountRepository;

    @Transactional
    public EmployeeRegisterResponse addEmployee(EmployeeRegisterRequest requestDTO){
        return employeeAccountService.registerEmployeeAccount(requestDTO);
    }

    @Transactional
    public EmployeeRegisterResponse updateEmployee(UpdateEmployeeRequest requestDTO){
        Company company = authenticationService.getAuthenticatedCompany();
        EmployeeAccount employeeAccount = employeeAccountRepository.findById(requestDTO.getId()).orElseThrow(() -> new AuthorizationException("Employee not found"));
        if (!employeeAccount.getCompany().getId().equals(company.getId())) {
            throw new AuthorizationException("You do not have permission to update this employee");
        }
        Optional.ofNullable(requestDTO.getEmployeeRole()).ifPresent(employeeAccount::setEmployeeRole);
        EmployeeAccount updatedEmployee = employeeAccountRepository.save(employeeAccount);
        return buildResponse(updatedEmployee);
    }

    public List<EmployeeRegisterResponse> getEmployeesForCompany() {
        Company company = authenticationService.getAuthenticatedCompany();
        List<EmployeeAccount> employees = company.getEmployees();
        return employees.stream().map(this::buildResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    private EmployeeResponseDTO mapToResponseDTO(Employee employee){
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .secondName(employee.getSecondName())
                .lastName(employee.getLastName())
                .egn(employee.getEgn())
                .role(employee.getRole())
                .build();
    }

    public EmployeeRegisterResponse buildResponse(EmployeeAccount account){
        Office office = account.getOffice();
        OfficeResponseDTO officeResponseDTO = OfficeResponseDTO.builder()
                .id(office.getId())
                .address(office.getAddress())
                .phoneNumber(office.getPhoneNumber())
                .build();
        return EmployeeRegisterResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .firstName(account.getFirstName())
                .secondName(account.getSecondName())
                .lastName(account.getLastName())
                .officeId(officeResponseDTO)
                .egn(account.getEgn())
                .role(account.getRole())
                .employeeRole(account.getEmployeeRole()).build();
    }
}

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
import com.company.logistics.utils.GlobalMapper;
import com.company.logistics.utils.Validator;
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
        Validator.validateEmployeeCompanyRelationship(employeeAccount, company);
        Optional.ofNullable(requestDTO.getEmployeeRole()).ifPresent(employeeAccount::setEmployeeRole);
        EmployeeAccount updatedEmployee = employeeAccountRepository.save(employeeAccount);
        return GlobalMapper.buildEmployeeResponse(updatedEmployee);
    }

    public List<EmployeeRegisterResponse> getEmployeesForCompany() {
        Company company = authenticationService.getAuthenticatedCompany();
        List<EmployeeAccount> employees = company.getEmployees();
        return employees.stream().map(GlobalMapper::buildEmployeeResponse).collect(Collectors.toList());
    }
}

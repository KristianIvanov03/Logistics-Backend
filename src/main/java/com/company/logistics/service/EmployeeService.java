package com.company.logistics.service;

import com.company.logistics.model.company.Company;
import com.company.logistics.model.employee.Employee;
import com.company.logistics.model.employee.EmployeeRequestDTO;
import com.company.logistics.model.employee.EmployeeResponseDTO;
import com.company.logistics.model.office.Office;
import com.company.logistics.model.office.OfficeResponseDTO;
import com.company.logistics.repository.CompanyRepository;
import com.company.logistics.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO requestDTO){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Company company = companyRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Company not found"));

        Employee employee = Employee.builder()
                .firstName(requestDTO.getFirstName())
                .secondName(requestDTO.getSecondName())
                .lastName(requestDTO.getLastName())
                .role(requestDTO.getRole())
                .egn(requestDTO.getEgn())
                .company(company)
                .build();

        Employee employee1 = employeeRepository.save(employee);
        return EmployeeResponseDTO.builder()
                .id(employee1.getId())
                .firstName(employee1.getFirstName())
                .secondName(employee1.getSecondName())
                .lastName(employee1.getLastName())
                .egn(employee1.getEgn())
                .role(employee1.getRole())
                .build();
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(EmployeeRequestDTO requestDTO){
        Employee employee = employeeRepository.findById(requestDTO.getId()).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        if(requestDTO.getFirstName() != null){
            employee.setFirstName(requestDTO.getFirstName());
        }
        if(requestDTO.getSecondName() != null){
            employee.setSecondName(requestDTO.getSecondName());
        }
        if(requestDTO.getLastName() != null){
            employee.setLastName(requestDTO.getLastName());
        }
        if(requestDTO.getEgn() != null){
            employee.setEgn(requestDTO.getEgn());
        }
        if(requestDTO.getRole() != null){
            employee.setRole(requestDTO.getRole());
        }
        Employee employee1 = employeeRepository.save(employee);
        return EmployeeResponseDTO.builder()
                .id(employee1.getId())
                .firstName(employee1.getFirstName())
                .secondName(employee1.getSecondName())
                .lastName(employee1.getLastName())
                .egn(employee1.getEgn())
                .role(employee1.getRole())
                .build();
    }

    public List<EmployeeResponseDTO> getEmployeesForCompany() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Company company = companyRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Company not found"));

        List<Employee> employees =  employeeRepository.findByCompany(company);
        return employees.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
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
}

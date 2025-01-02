package com.company.logistics.service;

import com.company.logistics.model.RevenueInPeriodRequest;
import com.company.logistics.model.employee.EmployeeResponseDTO;
import com.company.logistics.model.entities.ClientAccount;
import com.company.logistics.model.entities.Company;
import com.company.logistics.model.entities.Employee;
import com.company.logistics.model.entities.EmployeeAccount;
import com.company.logistics.model.entities.Package;
import com.company.logistics.model.enums.PackageStatus;
import com.company.logistics.model.packages.ClientInfo;
import com.company.logistics.model.packages.PackageResonseDto;
import com.company.logistics.repository.*;
import com.company.logistics.utils.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final EmployeeRepository employeeRepository;
    private final ClientAccountRepository clientAccountRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeAccountRepository employeeAccountRepository;
    private final PackageRepository packageRepository;
    public List<EmployeeResponseDTO> getAllEmployees(){
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(ReportMapper::toEmployee)
                .collect(Collectors.toList());
    }

    public  List<ClientInfo> getAllClients(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Company company = companyRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        List<ClientAccount> clients = clientAccountRepository.findAllByCompanyId(company);
        return company.getClients().stream()
                .map(ReportMapper::buildClientInfo)
                .collect(Collectors.toList());
    }

    public List<PackageResonseDto> getAllPackages(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Company company = companyRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        return company.getPackages().stream()
                .map(ReportMapper::buildPackage)
                .collect(Collectors.toList());
    }

    public List<PackageResonseDto> getAllPackagesByRegisteredEmployee(Long id){
        EmployeeAccount account = employeeAccountRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        List<Package> packages = packageRepository.findAllByRegisteredByEmployee(account);
        return packages.stream()
                .map(ReportMapper::buildPackage)
                .collect(Collectors.toList());
    }

    public List<PackageResonseDto> getAllPendingPackages(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Company company = companyRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        return company.getPackages().stream()
                .filter(pack -> pack.getStatus() == PackageStatus.PENDING)
                .map(ReportMapper::buildPackage)
                .collect(Collectors.toList());
    }

    public BigDecimal getRevenueInPeriod(RevenueInPeriodRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Company company = companyRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        return company.getPackages().stream()
                .filter(p -> !p.getDeliveryDate().isBefore(request.getStartDate())
                        && !p.getDeliveryDate().isAfter(request.getEndDate())) // Ensure package is delivered
                .map(Package::getPrice) // Extract the price
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<PackageResonseDto> getAllSentPackages(Long id){
        ClientAccount clientAccount = clientAccountRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        List<Package> packages = clientAccount.getSentPackages();
        return packages.stream()
                .map(ReportMapper::buildPackage)
                .collect(Collectors.toList());
    }
    public List<PackageResonseDto> getAllReceivedPackages(Long id){
        ClientAccount clientAccount = clientAccountRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        List<Package> packages = clientAccount.getReceivedPackages();
        return packages.stream()
                .map(ReportMapper::buildPackage)
                .collect(Collectors.toList());
    }
}

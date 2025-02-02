package com.company.logistics.service;

import com.company.logistics.exception.AuthorizationException;
import com.company.logistics.model.entities.*;
import com.company.logistics.model.entities.Package;
import com.company.logistics.model.enums.DeliveryType;
import com.company.logistics.model.enums.Role;
import com.company.logistics.model.enums.ShippingMethod;
import com.company.logistics.model.packages.ClientInfo;
import com.company.logistics.model.packages.PackageRequestDto;
import com.company.logistics.model.packages.PackageResonseDto;
import com.company.logistics.model.packages.UpdateStatusRequest;
import com.company.logistics.repository.*;
import com.company.logistics.model.enums.PackageStatus;
import com.company.logistics.utils.AuthenticationService;
import com.company.logistics.utils.PriceCalculator;
import com.company.logistics.utils.ReportMapper;
import com.company.logistics.utils.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageService {
    private final PackageRepository packageRepository;
    private final ClientAccountRepository clientAccountRepository;
    private final OfficeRepository officeRepository;
    private final EmployeeAccountRepository employeeAccountRepository;
    private final CompanyRepository companyRepository;
    private final AuthenticationService authenticationService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PackageResonseDto createPackage(PackageRequestDto request){
        Validator.validatePackage(request);
        EmployeeAccount employeeAccount = authenticationService.getAuthenticatedEmployee();
        ClientAccount sender = clientAccountRepository.findById(request.getSenderId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        ClientAccount receiver = clientAccountRepository.findById(request.getReceiverId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        Office senderOffice = officeRepository.findById(employeeAccount.getOffice().getId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        Office receiverOffice = null;
        if(request.getShippingMethod() == ShippingMethod.OFFICE){
            receiverOffice = officeRepository.findById(request.getReceiverOfficeId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        }
        Company company = companyRepository.findById(employeeAccount.getCompany().getId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        Package pack = Package.builder()
                .company(company)
                .senderId(sender)
                .receiverId(receiver)
                .deliveryAddress(request.getShippingMethod() == ShippingMethod.ADDRESS ? request.getDeliveryAddress() : null)
                .senderOffice(senderOffice)
                .recieverOffice(receiverOffice)
                .weight(request.getWeight())
                .price(PriceCalculator.calculatePrice(request))
                .isDelivered(false)
                .isPaid(false)
                .registrationDate(LocalDateTime.now())
                .registeredByEmployee(employeeAccount)
                .status(PackageStatus.NEW)
                .deliveryType(request.getDeliveryType())
                .deliveryDate(calculateDeliveryDate(request.getDeliveryType()))
                .shippingMethod(request.getShippingMethod()).build();
        Package pack1 = packageRepository.save(pack);
        PackageResonseDto response = ReportMapper.buildPackage(pack1);
        return response;
    }

    public PackageResonseDto updateStatus(UpdateStatusRequest request){
        EmployeeAccount employeeAccount = authenticationService.getAuthenticatedEmployee();
        Package pack = packageRepository.findById(request.getPackageId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        Validator.validatePackagePermission(employeeAccount, pack);
        pack.setStatus(request.getStatus());
        Package pack1 = packageRepository.save(pack);
        return ReportMapper.buildPackage(pack1);
    }

    public PackageResonseDto finishPackage(Long id){
        EmployeeAccount employeeAccount = authenticationService.getAuthenticatedEmployee();
        Package pack = packageRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        Validator.validatePackagePermission(employeeAccount, pack);
        pack.setStatus(PackageStatus.DELIVERED);
        pack.setDeliveryDate(LocalDate.now());
        pack.setIsPaid(true);
        Package pack1 = packageRepository.save(pack);
        return ReportMapper.buildPackage(pack1);
    }

    public List<PackageResonseDto> getAllPackages() throws AccessDeniedException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Role userRole = getUserRole();

        if (userRole == Role.CLIENT) {
            ClientAccount client = clientAccountRepository.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Client not found"));

            // Combine sent and received packages
            List<PackageResonseDto> sentPackages = client.getSentPackages().stream()
                    .map(ReportMapper::buildPackage)
                    .collect(Collectors.toList());

            List<PackageResonseDto> receivedPackages = client.getReceivedPackages().stream()
                    .map(ReportMapper::buildPackage)
                    .collect(Collectors.toList());

            // Merge the two lists
            List<PackageResonseDto> allPackages = new ArrayList<>();
            allPackages.addAll(sentPackages);
            allPackages.addAll(receivedPackages);

            return allPackages;

        } else if (userRole == Role.COMPANY) {
            Company company = companyRepository.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Company not found"));

            return company.getPackages().stream()
                    .map(ReportMapper::buildPackage)
                    .collect(Collectors.toList());
        } else if (userRole == Role.EMPLOYEE){
            EmployeeAccount employeeAccount = authenticationService.getAuthenticatedEmployee();
            Company company = employeeAccount.getCompany();
            return company.getPackages().stream().map(ReportMapper::buildPackage).toList();
        }

        throw new AccessDeniedException("Unauthorized role");
    }

    private ClientInfo buildClientInfo(ClientAccount account){
        return ClientInfo.builder().
                firstName(account.getFirstName())
                .lastName(account.getLastName())
                .phoneNumber(account.getPhoneNumber())
                .address(account.getAddress()).build();
    }

    private Role getUserRole() throws AccessDeniedException {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(Role::valueOf)
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("No role assigned to user"));
    }

    private LocalDate calculateDeliveryDate(DeliveryType deliveryType){
        if (deliveryType == DeliveryType.EXPRESS){
            return LocalDate.now().plusDays(4);
        }else if (deliveryType == DeliveryType.OVERNIGHT){
            return LocalDate.now().plusDays(2);
        }else {
            return LocalDate.now().plusDays(7);
        }
    }
}

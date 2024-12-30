package com.company.logistics.service;

import com.company.logistics.model.entities.*;
import com.company.logistics.model.entities.Package;
import com.company.logistics.model.packages.ClientInfo;
import com.company.logistics.model.packages.PackageRequestDto;
import com.company.logistics.model.packages.PackageResonseDto;
import com.company.logistics.model.packages.UpdateStatusRequest;
import com.company.logistics.repository.*;
import com.company.logistics.model.enums.PackageStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PackageService {
    private final PackageRepository packageRepository;
    private final ClientAccountRepository clientAccountRepository;
    private final OfficeRepository officeRepository;
    private final EmployeeAccountRepository employeeAccountRepository;
    private final CompanyRepository companyRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PackageResonseDto createPackage(PackageRequestDto request){
        ClientAccount sender = clientAccountRepository.findById(request.getSenderId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        ClientAccount receiver = clientAccountRepository.findById(request.getReceiverId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        Office senderOffice = officeRepository.findById(request.getSenderOfficeId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        Office receiverOffice = officeRepository.findById(request.getReceiverOfficeId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        EmployeeAccount employeeAccount = employeeAccountRepository.findById(request.getRegisteredByEmployeeId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        Company company = companyRepository.findById(employeeAccount.getCompany().getId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        Package pack = Package.builder()
                .company(company)
                .senderId(sender)
                .receiverId(receiver)
                .deliveryAddress(receiver.getAddress())
                .senderOffice(senderOffice)
                .recieverOffice(receiverOffice)
                .weight(request.getWeight())
                .price(request.getPrice())
                .isDelivered(false)
                .registrationDate(LocalDateTime.now())
                .registeredByEmployee(employeeAccount)
                .status(PackageStatus.NEW)
                .deliveryType(request.getDeliveryType())
                .shippingMethod(request.getShippingMethod()).build();
        Package pack1 = packageRepository.save(pack);
        PackageResonseDto response = PackageResonseDto.builder()
                .id(pack1.getId())
                .deliveryAddress(pack1.getDeliveryAddress())
                .weight(pack1.getWeight())
                .price(pack1.getPrice())
                .deliveryType(pack1.getDeliveryType())
                .shippingMethod(pack1.getShippingMethod())
                .status(pack1.getStatus())
                .senderInfo(buildClientInfo(pack1.getSenderId()))
                .receiverInfo(buildClientInfo(pack1.getReceiverId())).build();
        return response;
    }

    public Package updateStatus(UpdateStatusRequest request){
        Package pack = packageRepository.findById(request.getPackageId()).orElseThrow(() -> new UsernameNotFoundException("Company not found"));
        pack.setStatus(request.getStatus());
        return packageRepository.save(pack);
    }

    private ClientInfo buildClientInfo(ClientAccount account){
        return ClientInfo.builder().
                firstName(account.getFirstName())
                .lastName(account.getLastName())
                .phoneNumber(account.getPhoneNumber())
                .address(account.getAddress()).build();
    }
}

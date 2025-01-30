package com.company.logistics.utils;

import ch.qos.logback.core.net.server.Client;
import com.company.logistics.model.clientaccount.ClientRegisterRequest;
import com.company.logistics.model.clientaccount.ClientResponseDTO;
import com.company.logistics.model.company.CompanyInfoResponse;
import com.company.logistics.model.employeeaccaunts.EmployeeRegisterResponse;
import com.company.logistics.model.entities.ClientAccount;
import com.company.logistics.model.entities.Company;
import com.company.logistics.model.entities.EmployeeAccount;
import com.company.logistics.model.entities.Office;
import com.company.logistics.model.office.OfficeResponseDTO;

public class GlobalMapper {
    public static CompanyInfoResponse toCompanyInfo(Company company){
        return CompanyInfoResponse.builder()
                .id(company.getId())
                .taxNumber(company.getTaxNumber())
                .name(company.getName())
                .address(company.getAddress())
                .email(company.getEmail())
                .phoneNumber(company.getPhoneNumber())
                .role(company.getRole())
                .build();
    }

    public static OfficeResponseDTO toOfficeResponse(Office office){
        return new OfficeResponseDTO(
                office.getId(),
                office.getAddress(),
                office.getPhoneNumber(),
                office.getCompany().getId()
        );
    }

    public static EmployeeRegisterResponse buildEmployeeResponse(EmployeeAccount account){
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

    public static ClientResponseDTO buildClientResponse(ClientAccount client){
        return ClientResponseDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .phoneNumber(client.getPhoneNumber())
                .address(client.getAddress())
                .role(client.getRole()).build();
    }
}

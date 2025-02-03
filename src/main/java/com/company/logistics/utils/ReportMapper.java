package com.company.logistics.utils;

import com.company.logistics.model.employee.EmployeeResponseDTO;
import com.company.logistics.model.entities.ClientAccount;
import com.company.logistics.model.entities.Employee;
import com.company.logistics.model.entities.Package;
import com.company.logistics.model.packages.ClientInfo;
import com.company.logistics.model.packages.PackageResonseDto;

public class ReportMapper {
    public static EmployeeResponseDTO toEmployee(Employee employee){
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .secondName(employee.getSecondName())
                .lastName(employee.getLastName())
                .egn(employee.getEgn())
                .role(employee.getRole()).build();
    }

    public static ClientInfo buildClientInfo(ClientAccount account){
        return ClientInfo.builder().
                firstName(account.getFirstName())
                .lastName(account.getLastName())
                .phoneNumber(account.getPhoneNumber())
                .address(account.getAddress()).build();
    }

    public static PackageResonseDto buildPackage(Package pack){
        return PackageResonseDto.builder()
                .id(pack.getId())
                .deliveryAddress(pack.getDeliveryAddress())
                .receiverOffice(GlobalMapper.toOfficeResponse(pack.getRecieverOffice()))
                .weight(pack.getWeight())
                .price(pack.getPrice())
                .isPaid(pack.getIsPaid())
                .deliveryDate(pack.getDeliveryDate())
                .deliveryType(pack.getDeliveryType())
                .shippingMethod(pack.getShippingMethod())
                .status(pack.getStatus())
                .senderInfo(buildClientInfo(pack.getSenderId()))
                .receiverInfo(buildClientInfo(pack.getReceiverId())).build();
    }
}

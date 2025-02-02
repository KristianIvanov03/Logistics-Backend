package com.company.logistics.utils;

import com.company.logistics.exception.AuthorizationException;
import com.company.logistics.exception.ValidationException;
import com.company.logistics.model.entities.Company;
import com.company.logistics.model.entities.EmployeeAccount;
import com.company.logistics.model.entities.Package;
import com.company.logistics.model.enums.ShippingMethod;
import com.company.logistics.model.packages.PackageRequestDto;

public class Validator {
    public static void validatePackagePermission(EmployeeAccount employeeAccount, Package pack){
        if (employeeAccount.getCompany().getId() != pack.getCompany().getId()){
            throw new AuthorizationException("You do not have permission to this package");
        }
    }
    public static void validateEmployeeCompanyRelationship(EmployeeAccount employeeAccount, Company company){
        if (!employeeAccount.getCompany().getId().equals(company.getId())) {
            throw new AuthorizationException("You do not have permission to update this employee");
        }
    }

    public static void validatePackage(PackageRequestDto requestDto){
        validateShipmentMethod(requestDto);
    }

    private static void validateShipmentMethod(PackageRequestDto requestDto){
        if (requestDto.getShippingMethod() == ShippingMethod.ADDRESS && requestDto.getDeliveryAddress() == null){
            throw new ValidationException("Missing address for shipping method ADDRESS");
        }
        if (requestDto.getShippingMethod() == ShippingMethod.OFFICE && requestDto.getReceiverOfficeId() == null){
            throw new ValidationException("Missing office for shipping method OFFICE");
        }
    }
}

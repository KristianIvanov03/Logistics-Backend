package com.company.logistics.utils;

import com.company.logistics.exception.AuthorizationException;
import com.company.logistics.model.entities.Company;
import com.company.logistics.model.entities.EmployeeAccount;
import com.company.logistics.model.entities.Package;

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
}

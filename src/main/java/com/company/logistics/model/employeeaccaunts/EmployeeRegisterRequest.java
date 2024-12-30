package com.company.logistics.model.employeeaccaunts;

import com.company.logistics.model.enums.EmployeeRole;
import com.company.logistics.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRegisterRequest {
    private String username;
    private String password;
    private String firstName;
    private String secondName;
    private String lastName;
    private Long officeId;
    private Long companyId;
    private String egn;
    private Role role;
    private EmployeeRole employeeRole;
}

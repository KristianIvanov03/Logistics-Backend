package com.company.logistics.model.employeeaccaunts;

import com.company.logistics.model.entities.Office;
import com.company.logistics.model.enums.EmployeeRole;
import com.company.logistics.model.enums.Role;
import com.company.logistics.model.office.OfficeResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRegisterResponse {
    private Long id;
    private String username;
    private String firstName;
    private String secondName;
    private String lastName;
    private OfficeResponseDTO officeId;
    private String egn;
    private Role role;
    private EmployeeRole employeeRole;
}

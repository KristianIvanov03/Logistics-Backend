package com.company.logistics.model.employee;

import com.company.logistics.model.enums.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRequestDTO {
    private Long id;
    private String firstName;
    private String secondName;
    private String lastName;
    private String egn;
    private EmployeeRole role;
}

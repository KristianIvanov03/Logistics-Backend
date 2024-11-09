package com.company.logistics.model.employee;

import com.company.logistics.utils.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponseDTO {
    public Long id;
    public String firstName;
    public String secondName;
    public String lastName;
    public String egn;
    public EmployeeRole role;
}

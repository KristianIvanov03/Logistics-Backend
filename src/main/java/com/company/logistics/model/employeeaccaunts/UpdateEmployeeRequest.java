package com.company.logistics.model.employeeaccaunts;

import com.company.logistics.model.enums.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEmployeeRequest {
    private EmployeeRole employeeRole;
    private Long id;
}

package com.company.logistics.model.employeeaccaunts;

import com.company.logistics.model.enums.EmployeeRole;
import com.company.logistics.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRegisterRequest {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Second name cannot be empty")
    private String secondName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @NotNull(message = "Office ID cannot be null")
    private Long officeId;

    @Pattern(regexp = "^[0-9]{10}$", message = "EGN must be a valid 10-digit number")
    private String egn;

    @NotNull(message = "Role cannot be null")
    private Role role;

    @NotNull(message = "Employee Role cannot be null")
    private EmployeeRole employeeRole;
}

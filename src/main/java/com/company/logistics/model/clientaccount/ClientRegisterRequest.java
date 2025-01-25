package com.company.logistics.model.clientaccount;

import com.company.logistics.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRegisterRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "\\+?[0-9]{7,15}", message = "Phone number must be valid and contain between 7 and 15 digits")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotNull(message = "Role cannot be null")
    private Role role;

    @NotNull(message = "Company ID cannot be null")
    private Long companyId;
}
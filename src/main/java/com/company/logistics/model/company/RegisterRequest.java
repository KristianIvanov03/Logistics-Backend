package com.company.logistics.model.company;

import com.company.logistics.model.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    private String password;

    @NotBlank(message = "Tax number is required")
    @Pattern(regexp = "\\d{10}", message = "Tax number must be a 10-digit number")
    private String taxNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 100, message = "Address cannot exceed 100 characters")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\+?[0-9]{7,15}", message = "Phone number must be valid")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Role is required")
    private Role role;
}

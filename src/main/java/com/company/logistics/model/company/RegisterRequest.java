package com.company.logistics.model.company;

import com.company.logistics.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String password;
    private String taxNumber;
    private String address;
    private String phoneNumber;
    private String email;
    private Role role;
}

package com.company.logistics.model.clientaccount;

import com.company.logistics.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRegisterRequest {
    private String name;

    private String password;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;

    private Role role;
}

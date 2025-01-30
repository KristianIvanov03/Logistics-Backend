package com.company.logistics.model.clientaccount;

import com.company.logistics.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponseDTO {
    private Long id;
    private String name;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private Role role;
}

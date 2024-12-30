package com.company.logistics.model.company;

import com.company.logistics.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyInfoResponse {
    private Long id;
    private String name;
    private String taxNumber;
    private String address;
    private String phoneNumber;
    private String email;
    private Role role;
}

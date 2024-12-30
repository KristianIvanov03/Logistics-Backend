package com.company.logistics.model.packages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientInfo {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
}

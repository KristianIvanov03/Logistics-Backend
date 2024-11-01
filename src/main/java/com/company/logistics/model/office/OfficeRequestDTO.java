package com.company.logistics.model.office;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficeRequestDTO {
    private String address;
    private String phoneNumber;
}

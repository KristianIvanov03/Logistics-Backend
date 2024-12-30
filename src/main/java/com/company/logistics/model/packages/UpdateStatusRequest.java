package com.company.logistics.model.packages;

import com.company.logistics.model.enums.PackageStatus;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    private Long packageId;
    private PackageStatus status;
}

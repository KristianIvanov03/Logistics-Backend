package com.company.logistics.model.packages;

import com.company.logistics.model.enums.DeliveryType;
import com.company.logistics.model.enums.PackageStatus;
import com.company.logistics.model.enums.ShippingMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PackageRequestDto {
    private Long senderId;
    private Long receiverId;
    private String deliveryAddress;
    private Long senderOfficeId;
    private Long receiverOfficeId;
    private Double weight;
    private String deliveryDate;
    private Long registeredByEmployeeId;
    private BigDecimal price;
    private DeliveryType deliveryType;
    private ShippingMethod shippingMethod;
}

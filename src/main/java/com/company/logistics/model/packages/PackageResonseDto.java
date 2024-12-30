package com.company.logistics.model.packages;

import com.company.logistics.model.enums.DeliveryType;
import com.company.logistics.model.enums.PackageStatus;
import com.company.logistics.model.enums.ShippingMethod;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PackageResonseDto {
    private Long id;
    private String deliveryAddress;
    private Double weight;
    private BigDecimal price;
    private DeliveryType deliveryType;
    private ShippingMethod shippingMethod;
    private PackageStatus status;
    private ClientInfo senderInfo;
    private ClientInfo receiverInfo;
}

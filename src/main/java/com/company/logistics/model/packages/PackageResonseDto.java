package com.company.logistics.model.packages;

import com.company.logistics.model.enums.DeliveryType;
import com.company.logistics.model.enums.PackageStatus;
import com.company.logistics.model.enums.ShippingMethod;
import com.company.logistics.model.office.OfficeResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageResonseDto {
    private Long id;
    private String deliveryAddress;
    private OfficeResponseDTO senderOffice;
    private OfficeResponseDTO receiverOffice;
    private Double weight;
    private BigDecimal price;
    private DeliveryType deliveryType;
    private ShippingMethod shippingMethod;
    private PackageStatus status;
    private ClientInfo senderInfo;
    private ClientInfo receiverInfo;
    private Boolean isPaid;
    private LocalDate deliveryDate;
}

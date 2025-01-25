package com.company.logistics.model.packages;

import com.company.logistics.model.enums.DeliveryType;
import com.company.logistics.model.enums.PackageStatus;
import com.company.logistics.model.enums.ShippingMethod;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PackageRequestDto {
    @NotNull(message = "Sender ID is required")
    @Positive(message = "Sender ID must be a positive number")
    private Long senderId;
    @NotNull(message = "Receiver ID is required")
    @Positive(message = "Receiver ID must be a positive number")
    private Long receiverId;
    private String deliveryAddress;
    private Long receiverOfficeId;
    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be greater than 0")
    private Double weight;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have a maximum of 10 digits and 2 decimal places")
    private BigDecimal price;
    @NotNull(message = "Delivery type is required")
    private DeliveryType deliveryType;

    @NotNull(message = "Shipping method is required")
    private ShippingMethod shippingMethod;
}

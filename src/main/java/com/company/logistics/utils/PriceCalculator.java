package com.company.logistics.utils;

import com.company.logistics.model.enums.DeliveryType;
import com.company.logistics.model.enums.ShippingMethod;
import com.company.logistics.model.packages.PackageRequestDto;

import java.math.BigDecimal;
import java.sql.Statement;

public class PriceCalculator {
    private static final BigDecimal OFFICE_DELIVER_PRICE = BigDecimal.ONE;
    private static final BigDecimal ADDRESS_DELIVERY_PRICE = BigDecimal.valueOf(4);

    private static final BigDecimal STANDARD_DELIVERY_TYPE_PRICE = BigDecimal.ONE;
    private static final BigDecimal EXPRESS_DELIVERY_TYPE_PRICE = BigDecimal.valueOf(3);
    private static final BigDecimal OVERNIGHT_DELIVERY_TYPE_PRICE = BigDecimal.valueOf(5);
    public static BigDecimal calculatePrice(PackageRequestDto requestDto){
        BigDecimal price = requestDto.getPrice().add(shipmentPrice(requestDto.getShippingMethod())).add(deliveryTypePrice(requestDto.getDeliveryType()));
        return price;
    }

    private static BigDecimal shipmentPrice(ShippingMethod method){
        if (method == ShippingMethod.OFFICE){
            return OFFICE_DELIVER_PRICE;
        }else{
            return ADDRESS_DELIVERY_PRICE;
        }
    }

    private static BigDecimal deliveryTypePrice(DeliveryType deliveryType){
        if (deliveryType == DeliveryType.EXPRESS){
            return EXPRESS_DELIVERY_TYPE_PRICE;
        }else if (deliveryType == DeliveryType.STANDARD){
            return STANDARD_DELIVERY_TYPE_PRICE;
        }else {
            return OVERNIGHT_DELIVERY_TYPE_PRICE;
        }
    }
}

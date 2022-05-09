package com.guavapay.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guavapay.model.type.DeliveryState;
import com.guavapay.util.serializer.DateSerializer;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierOrderHistoryDto {

    private Long parcelId;
    private Long courierOrderId;
    @JsonSerialize(using = DateSerializer.class)
    private Date deliveryDate;
    @JsonSerialize(using = DateSerializer.class)
    private Date routeBeginDate;
    private DeliveryState state;
    private Measurement measurement;
    private String deliverAddress;
    private BigDecimal amount;
}

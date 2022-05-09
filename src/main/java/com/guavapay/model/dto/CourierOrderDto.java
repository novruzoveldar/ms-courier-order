package com.guavapay.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.guavapay.model.type.DeliveryState;
import com.guavapay.util.serializer.DateSerializer;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierOrderDto {

    private Long parcelId;
    @JsonSerialize(using = DateSerializer.class)
    private Date deliveryDate;
    @JsonSerialize(using = DateSerializer.class)
    private Date routeBeginDate;
    private DeliveryState state;
}

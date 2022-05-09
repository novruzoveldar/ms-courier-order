package com.guavapay.model.request;

import com.guavapay.model.type.DeliveryState;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStateChangeRequest {

    @NotNull
    private DeliveryState deliveryState;
    @NotNull
    private Long parcelId;
}

package com.guavapay.model.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.guavapay.model.dto.Measurement;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierOrderRequest {

    @NotNull
    private Long id;
    @NotNull
    private String deliverAddress;
    @NotNull
    private String amount;
    private Measurement measurement;
    @JsonSerialize(using = DateSerializer.class)
    private Date deliveryDate;
}

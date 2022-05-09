package com.guavapay.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierOrderChangeRequest {

    @NotNull
    private Long parcelId;
    @NotNull
    private String destination;
}

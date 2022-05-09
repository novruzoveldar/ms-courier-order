package com.guavapay.model.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAssignRequest {

    @NotNull
    private Long parcelId;
    @NotNull
    private Long accountId;
}

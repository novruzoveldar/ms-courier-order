package com.guavapay.model.request;

import com.guavapay.model.type.AvailabilityState;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierFilterRequest {

    @NotNull
    private List<AvailabilityState> availabilityState;
}

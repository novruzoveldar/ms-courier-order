package com.guavapay.model.dto;

import com.guavapay.model.type.AvailabilityState;
import com.guavapay.model.type.CourierType;
import com.guavapay.model.type.GenderType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierFilterDto {

    private String name;
    private String surname;
    private String email;
    private GenderType genderType;
    private String mobile;
    private AvailabilityState availabilityState;
    private CourierType type;
}

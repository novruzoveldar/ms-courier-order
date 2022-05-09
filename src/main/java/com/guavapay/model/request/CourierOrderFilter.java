package com.guavapay.model.request;

import com.guavapay.model.type.DeliveryState;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierOrderFilter {

    private Long parcelId;
    private List<DeliveryState> states;
    private Date from;
    private Date to;
    private int page;
    private int limit;
    private Long courierId;
}

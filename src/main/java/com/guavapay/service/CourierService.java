package com.guavapay.service;

import com.guavapay.model.dto.AllOrderHistoryDto;
import com.guavapay.model.dto.CourierFilterDto;
import com.guavapay.model.dto.CourierOrderDto;
import com.guavapay.model.dto.CourierOrderHistoryDto;
import com.guavapay.model.request.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface CourierService {

    CourierOrderDto orderCourier(CourierOrderRequest courierOrderRequest);
    CourierOrderDto changeOrder(CourierOrderChangeRequest orderChangeRequest);
    void cancelOrder(Long parcelId);
    CourierOrderHistoryDto getOrderDetail(Long parcelId);
    List<CourierOrderHistoryDto> getOrderHistory(CourierOrderFilter orderFilter);
    void changeState(@Valid @RequestBody OrderStateChangeRequest stateChangeRequest);
    List<AllOrderHistoryDto> getAllOrderHistory(CourierOrderFilter orderFilter);
    AllOrderHistoryDto assignCourier(OrderAssignRequest orderAssignRequest);

    List<CourierFilterDto> courierFilter(CourierFilterRequest courierFilterRequest);

}

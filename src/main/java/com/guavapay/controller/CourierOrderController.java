package com.guavapay.controller;

import com.guavapay.model.dto.AllOrderHistoryDto;
import com.guavapay.model.dto.CourierOrderDto;
import com.guavapay.model.dto.CourierOrderHistoryDto;
import com.guavapay.model.request.*;
import com.guavapay.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "courier/order")
public class CourierOrderController {

    private final CourierService courierService;

    @PostMapping(value = "/create", consumes = {"application/json"}, produces = {"application/json"})
    public CourierOrderDto createCourierOrder(@Valid @RequestBody CourierOrderRequest courierOrderRequest) {
        return courierService.orderCourier(courierOrderRequest);
    }

    @PostMapping(value = "/change", consumes = {"application/json"}, produces = {"application/json"})
    public CourierOrderDto changeOrder(@Valid @RequestBody CourierOrderChangeRequest orderChangeRequest) {
        return courierService.changeOrder(orderChangeRequest);
    }

    @PutMapping(value = "/cancel/{id}")
    public ResponseEntity<Object> cancelOrder(@PathVariable(value = "id") Long parcelId) {
        courierService.cancelOrder(parcelId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/detail")
    public CourierOrderHistoryDto orderDetail(@RequestParam(name = "id") Long parcelId) {
        return courierService.getOrderDetail(parcelId);
    }

    @PostMapping(value = "/history", consumes = {"application/json"}, produces = {"application/json"})
    public List<CourierOrderHistoryDto> orderHistory(@Valid @RequestBody CourierOrderFilter orderFilter) {
        return courierService.getOrderHistory(orderFilter);
    }

    @PostMapping(value = "/change/state")
    public ResponseEntity<Object> changeState(@Valid @RequestBody OrderStateChangeRequest stateChangeRequest) {
        courierService.changeState(stateChangeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/history/all", consumes = {"application/json"}, produces = {"application/json"})
    public List<AllOrderHistoryDto> allOrderHistory(@Valid @RequestBody CourierOrderFilter orderFilter) {
        return courierService.getAllOrderHistory(orderFilter);
    }

    @PostMapping(value = "/assign", consumes = {"application/json"}, produces = {"application/json"})
    public AllOrderHistoryDto assignCourier(@Valid @RequestBody OrderAssignRequest orderAssignRequest) {
        return courierService.assignCourier(orderAssignRequest);
    }

}

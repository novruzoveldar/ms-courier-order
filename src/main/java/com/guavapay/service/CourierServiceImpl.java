package com.guavapay.service;

import com.guavapay.error.OrderAlreadyCompletedException;
import com.guavapay.error.OrderNotFoundException;
import com.guavapay.model.dto.AllOrderHistoryDto;
import com.guavapay.model.dto.CourierFilterDto;
import com.guavapay.model.dto.CourierOrderDto;
import com.guavapay.model.dto.CourierOrderHistoryDto;
import com.guavapay.model.entity.Account;
import com.guavapay.model.entity.Courier;
import com.guavapay.model.entity.CourierOrder;
import com.guavapay.model.entity.Parcel;
import com.guavapay.model.mapper.CourierFilterMapper;
import com.guavapay.model.mapper.CourierOrderHistoryMapper;
import com.guavapay.model.mapper.CourierOrderMapper;
import com.guavapay.model.request.*;
import com.guavapay.model.type.DeliveryState;
import com.guavapay.repository.AccountRepository;
import com.guavapay.repository.CourierOrderRepository;
import com.guavapay.repository.CourierRepository;
import com.guavapay.repository.ParcelRepository;
import com.guavapay.security.Principal;
import com.guavapay.util.CourierOrderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourierServiceImpl implements CourierService {

    private final CourierOrderRepository courierOrderRepository;
    private final CourierOrderMapper courierOrderMapper;
    private final ParcelRepository parcelRepository;
    private final CourierOrderHistoryMapper orderHistoryMapper;
    private final AccountRepository accountRepository;
    private final CourierRepository courierRepository;
    private final CourierFilterMapper courierFilterMapper;

    @Override
    public CourierOrderDto orderCourier(CourierOrderRequest courierOrderRequest) {
        Parcel parcel = parcelRepository.getById(courierOrderRequest.getId());
        log.info("Parcel on the specified parameter was found. parcelId {}", parcel.getId());

        CourierOrder courierOrder = courierOrderMapper.toCourierOrderEntity(courierOrderRequest);

        courierOrder.setDeliveryOrderRrn(CourierOrderUtil.generateCourierOrderRrn());
        courierOrder.setRouteBeginDate(null);
        courierOrder.setRouteStopDate(null);
        courierOrder.setState(DeliveryState.PENDING);
        courierOrder.setParcel(parcel);
        courierOrderRepository.save(courierOrder);
        return courierOrderMapper.toCourierOrderDto(courierOrder, parcel.getId());
    }

    @Override
    public CourierOrderDto changeOrder(CourierOrderChangeRequest orderChangeRequest) {
        CourierOrder courierOrder = courierOrderRepository.getCourierOrderByParcelId(orderChangeRequest.getParcelId());
        log.info("Courier order on the specified parameter was found. parcelId {}", courierOrder.getId());

        if(!courierOrder.getState().equals(DeliveryState.CANCELED) &&
        !courierOrder.getState().equals(DeliveryState.DELIVERED)) {
            courierOrder.setDeliverAddress(orderChangeRequest.getDestination());
            return courierOrderMapper.toCourierOrderDto(courierOrderRepository.save(courierOrder), courierOrder.getParcel().getId());
        }
        throw new OrderAlreadyCompletedException("Courier Order already completed!");
    }

    @Override
    public void cancelOrder(Long parcelId) {
        CourierOrder courierOrder = courierOrderRepository.getCourierOrderByParcelId(parcelId);
        if(!courierOrder.getState().equals(DeliveryState.CANCELED) &&
                !courierOrder.getState().equals(DeliveryState.DELIVERED)) {
            courierOrder.setState(DeliveryState.CANCELED);
            courierOrderRepository.save(courierOrder);
        } else {
            throw new OrderAlreadyCompletedException("Courier Order already completed!");
        }
    }

    @Override
    public CourierOrderHistoryDto getOrderDetail(Long parcelId) {
        CourierOrder courierOrder = courierOrderRepository.getCourierOrderDetail(parcelId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found!"));
        log.info("Courier order on the specified parameter was found. parcelId {}", courierOrder.getId());

        return Stream.of(courierOrder)
                .map((CourierOrder order) -> orderHistoryMapper.toCourierOrderHistoryDto(order, courierOrder.getParcel().getId()))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException("Order not found!"));
    }

    @Override
    public List<CourierOrderHistoryDto> getOrderHistory(CourierOrderFilter orderFilter) {
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = accountRepository.findById(principal.getId()).orElseThrow(IllegalStateException::new);

        List<CourierOrder> courierOrderList = courierOrderRepository.findCourierOrderByCriteria(orderFilter, account);
        return orderHistoryMapper.toCourierOrderHistoryDtoList(courierOrderList,
                Objects.nonNull(orderFilter.getParcelId()) ? orderFilter.getParcelId() : null);
    }

    @Override
    public List<AllOrderHistoryDto> getAllOrderHistory(CourierOrderFilter orderFilter) {
        List<CourierOrder> courierOrderList = courierOrderRepository.findCourierOrderByCriteria(orderFilter, null);
        return orderHistoryMapper.toCourierAllOrderHistoryDtoList(courierOrderList);
    }

    @Override
    public void changeState(OrderStateChangeRequest stateChangeRequest) {
        CourierOrder courierOrder = courierOrderRepository.getCourierOrderByParcelId(stateChangeRequest.getParcelId());
        if(Objects.nonNull(courierOrder)) {
            courierOrder.setState(stateChangeRequest.getDeliveryState());
            courierOrderRepository.save(courierOrder);
        } else {
            throw new OrderNotFoundException("Order not found!");
        }
    }

    @Override
    public AllOrderHistoryDto assignCourier(OrderAssignRequest orderAssignRequest) {
        CourierOrder courierOrder = courierOrderRepository.getCourierOrderByParcelId(orderAssignRequest.getParcelId());
        if(Objects.nonNull(courierOrder)) {
            Courier courier = courierRepository.getCourierByAccountId(orderAssignRequest.getAccountId());
            courierOrder.setCourier(courier);
            courierOrderRepository.save(courierOrder);
            return orderHistoryMapper.toCourierAllOrderHistoryDto(courierOrderRepository.save(courierOrder));
        } else {
            throw new OrderNotFoundException("Order not found!");
        }
    }

    @Override
    public List<CourierFilterDto> courierFilter(CourierFilterRequest courierFilterRequest) {
        List<Courier> courierList = courierRepository.getCourierByState(courierFilterRequest.getAvailabilityState());
        return courierFilterMapper.toCourierFilterDtoList(courierList);
    }

}

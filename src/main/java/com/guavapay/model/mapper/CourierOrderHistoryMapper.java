package com.guavapay.model.mapper;

import com.guavapay.model.dto.AllOrderHistoryDto;
import com.guavapay.model.dto.CourierOrderHistoryDto;
import com.guavapay.model.dto.Measurement;
import com.guavapay.model.entity.CourierOrder;
import com.guavapay.model.type.AvailabilityState;
import com.guavapay.model.type.CourierType;
import com.guavapay.model.type.MeasurementUnit;
import com.guavapay.repository.CourierRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
imports = {AvailabilityState.class, Measurement.class, CourierType.class})
public abstract class CourierOrderHistoryMapper {

    @Mapping(target = "courierOrderId", source = "id")
    @Mapping(target = "amount", source = "deliverAmount", qualifiedByName = "mapAmount")
    @Mapping(target = "measurement", source = "courierOrder", qualifiedByName = "mapMeasurement")
    @Mapping(target = "parcelId", expression = "java(parcelId)")
    public abstract CourierOrderHistoryDto toCourierOrderHistoryDto(CourierOrder courierOrder, @Context Long parcelId);

    public abstract List<CourierOrderHistoryDto> toCourierOrderHistoryDtoList(List<CourierOrder> courierOrder, @Context Long parcelId);

    @Mapping(target = "parcelId", source = "parcel.id")
    @Mapping(target = "courierOrderId", source = "id")
    @Mapping(target = "amount", source = "deliverAmount", qualifiedByName = "mapAmount")
    @Mapping(target = "measurement", source = "courierOrder", qualifiedByName = "mapMeasurement")
    @Mapping(target = "name", source = "parcel.account.name")
    @Mapping(target = "surname", source = "parcel.account.surname")
    @Mapping(target = "email", source = "parcel.account.email")
    @Mapping(target = "mobile", source = "parcel.account.mobile")
    @Mapping(target = "gender", source = "parcel.account.gender")
    @Mapping(target = "courierId", source = "courier.id")
    @Mapping(target = "courierAvailabilityState", source = "courier.availabilityState")
    @Mapping(target = "courierType", source = "courier.type")
    public abstract AllOrderHistoryDto toCourierAllOrderHistoryDto(CourierOrder courierOrder);

    public abstract List<AllOrderHistoryDto> toCourierAllOrderHistoryDtoList(List<CourierOrder> courierOrder);

    @Named(value = "mapAmount")
    BigDecimal mapAmount(AtomicReference<BigDecimal> deliverAmount) {
        return deliverAmount.get();
    }

    @Named(value = "mapMeasurement")
    Measurement mapMeasurement(CourierOrder courierOrder) {
        return new Measurement(MeasurementUnit.of(courierOrder.getUnitOfMeasurement()),
                courierOrder.getDistance());
    }

}

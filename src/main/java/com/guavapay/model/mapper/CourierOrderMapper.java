package com.guavapay.model.mapper;

import com.guavapay.constant.Constants;
import com.guavapay.model.dto.CourierOrderDto;
import com.guavapay.model.dto.Measurement;
import com.guavapay.model.entity.CourierOrder;
import com.guavapay.model.request.CourierOrderRequest;
import com.guavapay.model.type.MeasurementUnit;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CourierOrderMapper {

    @Mapping(target = "deliverAmount", source = "amount", qualifiedByName = "mapPrice")
    @Mapping(target = "distance", source = "measurement.value")
    @Mapping(target = "unitOfMeasurement", source = "measurement", qualifiedByName = "mapUnit")
    CourierOrder toCourierOrderEntity(CourierOrderRequest courierOrderRequest);

    @Mapping(target = "parcelId", expression = "java(parcelId)")
    CourierOrderDto toCourierOrderDto(CourierOrder courierOrder, @Context Long parcelId);

    @Named(value = "mapPrice")
    default AtomicReference<BigDecimal> mapPrice(String productPrice) {
        return new AtomicReference<>(new BigDecimal(productPrice).divide(BigDecimal.valueOf(Constants.PRICE_SCALE_DIVIDER)));
    }

    @Named(value = "mapUnit")
    default String mapUnit(Measurement measurement) {
        return MeasurementUnit.of(measurement.getUnit());
    }
}

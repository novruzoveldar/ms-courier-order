package com.guavapay.model.mapper;

import com.guavapay.model.dto.CourierFilterDto;
import com.guavapay.model.entity.Courier;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CourierFilterMapper {

    @Mapping(target = "name", source = "account.name")
    @Mapping(target = "surname", source = "account.surname")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "mobile", source = "account.mobile")
    @Mapping(target = "genderType", source = "account.gender")
    CourierFilterDto toCourierFilterDto(Courier courier);

    List<CourierFilterDto> toCourierFilterDtoList(List<Courier> courier);
}

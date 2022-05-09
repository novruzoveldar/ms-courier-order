package com.guavapay.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Converter
public class AtomicIntegerConverter implements AttributeConverter<AtomicInteger, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AtomicInteger attribute) {
        return Objects.isNull(attribute) ? 0 : attribute.get();
    }

    @Override
    public AtomicInteger convertToEntityAttribute(Integer value) {
        return new AtomicInteger(Objects.isNull(value) ? 0 : value);
    }
}

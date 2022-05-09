package com.guavapay.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Converter
public class AtomicDecimalConverter implements AttributeConverter<AtomicReference<BigDecimal>, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(AtomicReference<BigDecimal> attribute) {
        return Objects.isNull(attribute) ? BigDecimal.ZERO : attribute.get();
    }

    @Override
    public AtomicReference<BigDecimal> convertToEntityAttribute(BigDecimal dbData) {
        return new AtomicReference<>(dbData);
    }
}

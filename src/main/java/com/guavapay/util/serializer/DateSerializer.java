package com.guavapay.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.guavapay.constant.Constants;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateSerializer extends JsonSerializer<Date> {

    @SneakyThrows
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.ISO_DATE_FORMAT);
        jsonGenerator.writeString(sdf.format(date));
    }
}

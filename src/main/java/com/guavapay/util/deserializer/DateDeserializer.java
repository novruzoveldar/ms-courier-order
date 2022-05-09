package com.guavapay.util.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.guavapay.constant.Constants;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateDeserializer extends JsonDeserializer<Date> {

    @SneakyThrows
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.ISO_DATE_FORMAT);
        return sdf.parse(jsonParser.getText());
    }
}

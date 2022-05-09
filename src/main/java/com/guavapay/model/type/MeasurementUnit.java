package com.guavapay.model.type;

import java.util.Arrays;

public enum MeasurementUnit {

    KILOGRAM,
    KILOMETER;

    public static String of(String unit) {
        return Arrays.stream(MeasurementUnit.values())
                .filter(measurementUnit -> unit.equalsIgnoreCase(measurementUnit.name().toString()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .name()
                .toLowerCase();
    }
}

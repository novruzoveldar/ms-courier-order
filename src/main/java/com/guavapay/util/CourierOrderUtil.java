package com.guavapay.util;

import java.util.UUID;

public class CourierOrderUtil {

    private static final String KEY = "CO";

    public static String generateCourierOrderRrn() {
        return KEY + UUID.randomUUID().toString().replaceAll("-", "");
    }
}

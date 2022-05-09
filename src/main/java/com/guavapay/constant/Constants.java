package com.guavapay.constant;

import java.math.BigDecimal;

public interface Constants {

    String TOKEN_CACHE_PREFIX = "accessToken";
    String INVALID_TOKEN = "BT100";
    Integer PRICE_SCALE_DIVIDER = 100;
    BigDecimal AMOUNT_PER_KILOGRAM = new BigDecimal("5.36");

    String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
}

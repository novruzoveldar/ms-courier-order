package com.guavapay.error;

public class OrderNotFoundException extends BaseException {

    public OrderNotFoundException(String errorMessage) {
        super(ErrorCode.ORDER_NOT_FOUND, errorMessage);
    }
}

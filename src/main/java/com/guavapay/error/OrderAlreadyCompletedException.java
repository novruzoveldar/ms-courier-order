package com.guavapay.error;

public class OrderAlreadyCompletedException extends BaseException {
    public OrderAlreadyCompletedException(String errorMessage) {
        super(ErrorCode.ORDER_ALREADY_COMPLETED, errorMessage);
    }
}

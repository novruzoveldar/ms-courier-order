package com.guavapay.error;

import feign.error.FeignExceptionConstructor;
import feign.error.ResponseBody;

import java.util.Objects;
import java.util.UUID;

public class BaseException extends RuntimeException {

    private String uuid;
    private String errorCode;
    private String errorMessage;

    public BaseException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.uuid = UUID.randomUUID().toString();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @FeignExceptionConstructor
    public BaseException(@ResponseBody String body) {
        if (Objects.nonNull(body)) {
            this.errorMessage = body;
        }
    }

    public String getUuid() {
        return uuid;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

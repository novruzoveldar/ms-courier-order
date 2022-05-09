package com.guavapay.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RestBaseError {

    private String code;
    private String message;

    public RestBaseError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

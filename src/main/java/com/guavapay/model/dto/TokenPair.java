package com.guavapay.model.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenPair implements Serializable {

    private String accessToken;
}

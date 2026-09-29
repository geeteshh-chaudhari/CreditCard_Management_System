package com.creditcard.dto;

public record MerchantResponse(
        Long merchantId,
        String merchantName,
        String category,
        String location
) {
}
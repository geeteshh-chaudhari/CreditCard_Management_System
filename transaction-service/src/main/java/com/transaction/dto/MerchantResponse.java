package com.transaction.dto;

public record MerchantResponse(
        Long merchantId,
        String merchantName,
        String category,
        String location
) {
}
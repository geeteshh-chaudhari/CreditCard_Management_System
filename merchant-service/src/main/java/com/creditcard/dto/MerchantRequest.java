package com.creditcard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MerchantRequest(
        @NotBlank @Size(max = 100) String merchantName,
        @NotBlank @Size(max = 50) String category,
        @NotBlank @Size(max = 100) String location
) {
}
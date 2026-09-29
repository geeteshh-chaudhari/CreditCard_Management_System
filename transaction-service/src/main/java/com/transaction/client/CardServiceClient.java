package com.transaction.client;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.transaction.dto.CreditCardResponse;

@FeignClient(name = "credit-card-service")
public interface CardServiceClient {

    @GetMapping("/cards/{cardId}")
    CreditCardResponse getCardById(
            @PathVariable("cardId") Long cardId
    );

    @PatchMapping("/cards/{cardId}/balance")
    CreditCardResponse updateBalance(
            @PathVariable("cardId") Long cardId,
            @RequestParam("availableCredit") BigDecimal availableCredit,
            @RequestParam("outstandingAmount") BigDecimal outstandingAmount
    );
}
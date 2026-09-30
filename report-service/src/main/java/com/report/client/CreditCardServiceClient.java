package com.report.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.report.dto.CreditCardResponse;

@FeignClient(name = "credit-card-service")
public interface CreditCardServiceClient {

    @GetMapping("/cards")
    List<CreditCardResponse> getAllCards();

    @GetMapping("/cards/customer/{customerId}")
    List<CreditCardResponse> getCardsByCustomerId(
            @PathVariable("customerId") Long customerId
    );
}
package com.report.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.report.dto.TransactionResponse;

@FeignClient(name = "transaction-service")
public interface TransactionServiceClient {

    @GetMapping("/transactions")
    List<TransactionResponse> getAllTransactions();
}
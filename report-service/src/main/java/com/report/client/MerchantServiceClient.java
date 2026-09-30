package com.report.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.report.dto.MerchantResponse;

@FeignClient(name = "merchant-service")
public interface MerchantServiceClient {

    @GetMapping("/merchants")
    List<MerchantResponse> getAllMerchants();

    @GetMapping("/merchants/{merchantId}")
    MerchantResponse getMerchantById(
            @PathVariable("merchantId") Long merchantId
    );
}
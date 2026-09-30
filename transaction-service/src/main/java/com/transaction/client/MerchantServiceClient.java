package com.transaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.transaction.dto.MerchantResponse;

@FeignClient(
	    name = "merchant-service"
	)
	public interface MerchantServiceClient {

	    @GetMapping("/merchants/{merchantId}")
	    MerchantResponse getMerchantById(
	            @PathVariable("merchantId") Long merchantId
	    );
	}
package com.report.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.report.dto.CustomerResponse;

@FeignClient(name = "customer-service")
public interface CustomerServiceClient {

    @GetMapping("/customers")
    List<CustomerResponse> getAllCustomers();

    @GetMapping("/customers/{customerId}")
    CustomerResponse getCustomerById(
            @PathVariable("customerId") Long customerId
    );
}
package com.creditcard.service;

import java.util.List;

import com.creditcard.dto.CustomerRequest;
import com.creditcard.dto.CustomerResponse;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse getCustomerById(Long customerId);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomer(Long customerId, CustomerRequest request);

    void deleteCustomer(Long customerId);

    List<CustomerResponse> searchCustomers(String keyword);
}
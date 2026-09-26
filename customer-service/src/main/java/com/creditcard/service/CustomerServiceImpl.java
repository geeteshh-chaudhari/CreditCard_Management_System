package com.creditcard.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.creditcard.dto.CustomerRequest;
import com.creditcard.dto.CustomerResponse;
import com.creditcard.entity.Customer;
import com.creditcard.exception.CustomerNotFoundException;
import com.creditcard.repository.CustomerRepository;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DataIntegrityViolationException(
                "Email already exists: " + request.getEmail()
            );
        }

        if (customerRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new DataIntegrityViolationException(
                "Mobile number already exists: " + request.getMobileNumber()
            );
        }

        if (customerRepository.existsByPanNumber(request.getPanNumber())) {
            throw new DataIntegrityViolationException(
                "PAN number already exists: " + request.getPanNumber()
            );
        }

        Customer customer = new Customer();

        customer.setCustomerName(request.getCustomerName());
        customer.setEmail(request.getEmail());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setPanNumber(request.getPanNumber());

        Customer savedCustomer = customerRepository.save(customer);

        return mapToResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));

        return mapToResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {

        return customerRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Override
    public CustomerResponse updateCustomer(
            Long customerId,
            CustomerRequest request) {

        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));

        customer.setCustomerName(request.getCustomerName());
        customer.setEmail(request.getEmail());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setPanNumber(request.getPanNumber());

        Customer updatedCustomer = customerRepository.save(customer);

        return mapToResponse(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));

        customerRepository.delete(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> searchCustomers(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return getAllCustomers();
        }

        return customerRepository
            .findByCustomerNameContainingIgnoreCase(keyword)
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    private CustomerResponse mapToResponse(Customer customer) {

        CustomerResponse response = new CustomerResponse();

        response.setCustomerId(customer.getCustomerId());
        response.setCustomerName(customer.getCustomerName());
        response.setEmail(customer.getEmail());
        response.setMobileNumber(customer.getMobileNumber());
        response.setPanNumber(customer.getPanNumber());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());

        return response;
    }
}
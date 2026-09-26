package com.creditcard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creditcard.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByCustomerNameContainingIgnoreCase(String customerName);

    List<Customer> findByEmailContainingIgnoreCase(String email);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByPanNumber(String panNumber);
}
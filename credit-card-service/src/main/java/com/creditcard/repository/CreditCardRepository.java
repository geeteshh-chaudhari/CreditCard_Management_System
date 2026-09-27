package com.creditcard.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.creditcard.entity.CreditCard;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    // Find credit card by unique card number
    Optional<CreditCard> findByCardNumber(String cardNumber);

    // Find all credit cards belonging to a customer
    List<CreditCard> findByCustomerId(Long customerId);

    // Check whether a card number already exists
    boolean existsByCardNumber(String cardNumber);
}
package com.transaction.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.transaction.dto.TransactionRequest;
import com.transaction.entity.Transaction;
import com.transaction.repository.TransactionRepository;

@Service
public class TransactionAuditService {

    private final TransactionRepository transactionRepository;

    public TransactionAuditService(
            TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedTransaction(
            TransactionRequest request,
            String failureReason) {

        Transaction transaction = new Transaction();

        transaction.setCardId(request.getCardId());
        transaction.setCustomerId(request.getCustomerId());
        transaction.setMerchantId(request.getMerchantId());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionStatus("FAILED");
        transaction.setFailureReason(failureReason);

        transactionRepository.save(transaction);
    }
}
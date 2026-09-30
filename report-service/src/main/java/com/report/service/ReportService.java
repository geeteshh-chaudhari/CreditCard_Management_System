package com.report.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.report.client.CustomerServiceClient;
import com.report.client.CreditCardServiceClient;
import com.report.client.MerchantServiceClient;
import com.report.client.TransactionServiceClient;

import com.report.dto.CustomerResponse;
import com.report.dto.CreditCardResponse;
import com.report.dto.MerchantResponse;
import com.report.dto.TransactionResponse;

@Service
public class ReportService {

    private final CustomerServiceClient customerServiceClient;
    private final CreditCardServiceClient creditCardServiceClient;
    private final MerchantServiceClient merchantServiceClient;
    private final TransactionServiceClient transactionServiceClient;

    public ReportService(
            CustomerServiceClient customerServiceClient,
            CreditCardServiceClient creditCardServiceClient,
            MerchantServiceClient merchantServiceClient,
            TransactionServiceClient transactionServiceClient) {

        this.customerServiceClient = customerServiceClient;
        this.creditCardServiceClient = creditCardServiceClient;
        this.merchantServiceClient = merchantServiceClient;
        this.transactionServiceClient = transactionServiceClient;
    }

    // ==========================================================
    // 1. Display all customer details
    // ==========================================================

    public List<CustomerResponse> getAllCustomers() {

        return customerServiceClient.getAllCustomers();
    }

    // ==========================================================
    // 2. Display all credit card details
    // ==========================================================

    public List<CreditCardResponse> getAllCards() {

        return creditCardServiceClient.getAllCards();
    }

    // ==========================================================
    // 3. Display all merchant details
    // ==========================================================

    public List<MerchantResponse> getAllMerchants() {

        return merchantServiceClient.getAllMerchants();
    }

    // ==========================================================
    // 4. Display complete transaction history
    // ==========================================================

    public List<TransactionResponse> getAllTransactions() {

        return transactionServiceClient.getAllTransactions();
    }

    // ==========================================================
    // 5. Display customers with highest outstanding balance
    // ==========================================================

    public List<Map<String, Object>> getHighestOutstandingCustomers() {

        List<CustomerResponse> customers =
                customerServiceClient.getAllCustomers();

        List<CreditCardResponse> cards =
                creditCardServiceClient.getAllCards();

        Map<Long, BigDecimal> customerBalance = new HashMap<>();

        for (CreditCardResponse card : cards) {

            Long customerId = card.getCustomerId();

            BigDecimal outstanding =
                    card.getOutstandingAmount();

            customerBalance.put(
                    customerId,
                    customerBalance.getOrDefault(
                            customerId,
                            BigDecimal.ZERO
                    ).add(outstanding)
            );
        }

        BigDecimal highest = BigDecimal.ZERO;

        for (BigDecimal amount : customerBalance.values()) {

            if (amount.compareTo(highest) > 0) {
                highest = amount;
            }
        }

        List<Map<String, Object>> result = new java.util.ArrayList<>();

        for (CustomerResponse customer : customers) {

            BigDecimal amount =
                    customerBalance.getOrDefault(
                            customer.getCustomerId(),
                            BigDecimal.ZERO
                    );

            if (amount.compareTo(highest) == 0) {

                Map<String, Object> data = new HashMap<>();

                data.put("customerId",
                        customer.getCustomerId());

                data.put("customerName",
                        customer.getCustomerName());

                data.put("outstandingAmount",
                        amount);

                result.add(data);
            }
        }

        return result;
    }

    // ==========================================================
    // 6. Display customers with lowest outstanding balance
    // ==========================================================

    public List<Map<String, Object>> getLowestOutstandingCustomers() {

        List<CustomerResponse> customers =
                customerServiceClient.getAllCustomers();

        List<CreditCardResponse> cards =
                creditCardServiceClient.getAllCards();

        Map<Long, BigDecimal> customerBalance = new HashMap<>();

        for (CreditCardResponse card : cards) {

            Long customerId = card.getCustomerId();

            BigDecimal outstanding =
                    card.getOutstandingAmount();

            customerBalance.put(
                    customerId,
                    customerBalance.getOrDefault(
                            customerId,
                            BigDecimal.ZERO
                    ).add(outstanding)
            );
        }

        BigDecimal lowest = null;

        for (CustomerResponse customer : customers) {

            BigDecimal amount =
                    customerBalance.getOrDefault(
                            customer.getCustomerId(),
                            BigDecimal.ZERO
                    );

            if (lowest == null ||
                    amount.compareTo(lowest) < 0) {

                lowest = amount;
            }
        }

        List<Map<String, Object>> result = new java.util.ArrayList<>();

        for (CustomerResponse customer : customers) {

            BigDecimal amount =
                    customerBalance.getOrDefault(
                            customer.getCustomerId(),
                            BigDecimal.ZERO
                    );

            if (amount.compareTo(lowest) == 0) {

                Map<String, Object> data = new HashMap<>();

                data.put("customerId",
                        customer.getCustomerId());

                data.put("customerName",
                        customer.getCustomerName());

                data.put("outstandingAmount",
                        amount);

                result.add(data);
            }
        }

        return result;
    }

    // ==========================================================
    // 7. Display merchant with highest sales amount
    // ==========================================================

    public List<Map<String, Object>> getHighestSalesMerchant() {

        List<MerchantResponse> merchants =
                merchantServiceClient.getAllMerchants();

        List<TransactionResponse> transactions =
                transactionServiceClient.getAllTransactions();

        Map<Long, BigDecimal> merchantSales =
                new HashMap<>();

        for (TransactionResponse transaction : transactions) {

            if ("PURCHASE".equalsIgnoreCase(
                    transaction.getTransactionType())
                    && "SUCCESS".equalsIgnoreCase(
                            transaction.getTransactionStatus())) {

                Long merchantId =
                        transaction.getMerchantId();

                BigDecimal amount =
                        transaction.getAmount();

                merchantSales.put(
                        merchantId,
                        merchantSales.getOrDefault(
                                merchantId,
                                BigDecimal.ZERO
                        ).add(amount)
                );
            }
        }

        BigDecimal highest = BigDecimal.ZERO;

        for (BigDecimal amount : merchantSales.values()) {

            if (amount.compareTo(highest) > 0) {
                highest = amount;
            }
        }

        List<Map<String, Object>> result =
                new java.util.ArrayList<>();

        for (MerchantResponse merchant : merchants) {

            BigDecimal sales =
                    merchantSales.getOrDefault(
                            merchant.getMerchantId(),
                            BigDecimal.ZERO
                    );

            if (sales.compareTo(highest) == 0) {

                Map<String, Object> data = new HashMap<>();

                data.put("merchantId",
                        merchant.getMerchantId());

                data.put("merchantName",
                        merchant.getMerchantName());

                data.put("salesAmount",
                        sales);

                result.add(data);
            }
        }

        return result;
    }

    // ==========================================================
    // 8. Display merchant with highest number of transactions
    // ==========================================================

    public List<Map<String, Object>>
    getHighestTransactionCountMerchant() {

        List<MerchantResponse> merchants =
                merchantServiceClient.getAllMerchants();

        List<TransactionResponse> transactions =
                transactionServiceClient.getAllTransactions();

        Map<Long, Integer> merchantCount =
                new HashMap<>();

        for (TransactionResponse transaction : transactions) {

            if ("PURCHASE".equalsIgnoreCase(
                    transaction.getTransactionType())
                    && "SUCCESS".equalsIgnoreCase(
                            transaction.getTransactionStatus())) {

                Long merchantId =
                        transaction.getMerchantId();

                merchantCount.put(
                        merchantId,
                        merchantCount.getOrDefault(
                                merchantId,
                                0
                        ) + 1
                );
            }
        }

        int highest = 0;

        for (Integer count : merchantCount.values()) {

            if (count > highest) {
                highest = count;
            }
        }

        List<Map<String, Object>> result =
                new java.util.ArrayList<>();

        for (MerchantResponse merchant : merchants) {

            int count =
                    merchantCount.getOrDefault(
                            merchant.getMerchantId(),
                            0
                    );

            if (count == highest) {

                Map<String, Object> data =
                        new HashMap<>();

                data.put("merchantId",
                        merchant.getMerchantId());

                data.put("merchantName",
                        merchant.getMerchantName());

                data.put("transactionCount",
                        count);

                result.add(data);
            }
        }

        return result;
    }

    // ==========================================================
    // 9. Display most frequently used credit card
    // ==========================================================

    public List<Map<String, Object>>
    getMostFrequentlyUsedCard() {

        List<CreditCardResponse> cards =
                creditCardServiceClient.getAllCards();

        List<TransactionResponse> transactions =
                transactionServiceClient.getAllTransactions();

        Map<Long, Integer> cardCount =
                new HashMap<>();

        for (TransactionResponse transaction : transactions) {

            if ("SUCCESS".equalsIgnoreCase(
                    transaction.getTransactionStatus())) {

                Long cardId =
                        transaction.getCardId();

                cardCount.put(
                        cardId,
                        cardCount.getOrDefault(
                                cardId,
                                0
                        ) + 1
                );
            }
        }

        int highest = 0;

        for (Integer count : cardCount.values()) {

            if (count > highest) {
                highest = count;
            }
        }

        List<Map<String, Object>> result =
                new java.util.ArrayList<>();

        for (CreditCardResponse card : cards) {

            int count =
                    cardCount.getOrDefault(
                            card.getCardId(),
                            0
                    );

            if (count == highest) {

                Map<String, Object> data =
                        new HashMap<>();

                data.put("cardId",
                        card.getCardId());

                data.put("customerId",
                        card.getCustomerId());

                data.put("usageCount",
                        count);

                result.add(data);
            }
        }

        return result;
    }

    // ==========================================================
    // 10. Display least frequently used credit card
    // ==========================================================

    public List<Map<String, Object>>
    getLeastFrequentlyUsedCard() {

        List<CreditCardResponse> cards =
                creditCardServiceClient.getAllCards();

        List<TransactionResponse> transactions =
                transactionServiceClient.getAllTransactions();

        Map<Long, Integer> cardCount =
                new HashMap<>();

        for (TransactionResponse transaction : transactions) {

            if ("SUCCESS".equalsIgnoreCase(
                    transaction.getTransactionStatus())) {

                Long cardId =
                        transaction.getCardId();

                cardCount.put(
                        cardId,
                        cardCount.getOrDefault(
                                cardId,
                                0
                        ) + 1
                );
            }
        }

        Integer lowest = null;

        for (CreditCardResponse card : cards) {

            int count =
                    cardCount.getOrDefault(
                            card.getCardId(),
                            0
                    );

            if (lowest == null ||
                    count < lowest) {

                lowest = count;
            }
        }

        List<Map<String, Object>> result =
                new java.util.ArrayList<>();

        for (CreditCardResponse card : cards) {

            int count =
                    cardCount.getOrDefault(
                            card.getCardId(),
                            0
                    );

            if (count == lowest) {

                Map<String, Object> data =
                        new HashMap<>();

                data.put("cardId",
                        card.getCardId());

                data.put("customerId",
                        card.getCustomerId());

                data.put("usageCount",
                        count);

                result.add(data);
            }
        }

        return result;
    }

    // ==========================================================
    // 11. Display total purchase amount for today
    // ==========================================================

    public BigDecimal getTodayPurchaseAmount() {

        LocalDate today = LocalDate.now();

        BigDecimal total = BigDecimal.ZERO;

        List<TransactionResponse> transactions =
                transactionServiceClient.getAllTransactions();

        for (TransactionResponse transaction : transactions) {

            if ("PURCHASE".equalsIgnoreCase(
                    transaction.getTransactionType())
                    && "SUCCESS".equalsIgnoreCase(
                            transaction.getTransactionStatus())
                    && transaction.getTransactionDate()
                            .toLocalDate()
                            .equals(today)) {

                total = total.add(
                        transaction.getAmount());
            }
        }

        return total;
    }

    // ==========================================================
    // 12. Display total payment amount for today
    // ==========================================================

    public BigDecimal getTodayPaymentAmount() {

        LocalDate today = LocalDate.now();

        BigDecimal total = BigDecimal.ZERO;

        List<TransactionResponse> transactions =
                transactionServiceClient.getAllTransactions();

        for (TransactionResponse transaction : transactions) {

            if ("PAYMENT".equalsIgnoreCase(
                    transaction.getTransactionType())
                    && "SUCCESS".equalsIgnoreCase(
                            transaction.getTransactionStatus())
                    && transaction.getTransactionDate()
                            .toLocalDate()
                            .equals(today)) {

                total = total.add(
                        transaction.getAmount());
            }
        }

        return total;
    }

    // ==========================================================
    // 13. Display all blocked credit cards
    // ==========================================================

    public List<CreditCardResponse> getAllBlockedCards() {

        List<CreditCardResponse> cards =
                creditCardServiceClient.getAllCards();

        return cards.stream()
                .filter(card ->
                        "BLOCKED".equalsIgnoreCase(
                                card.getCardStatus()))
                .toList();
    }

    // ==========================================================
    // 14. Display cards whose available credit is below
    //     20% of credit limit
    // ==========================================================

    public List<CreditCardResponse>
    getCardsBelow20PercentCredit() {

        List<CreditCardResponse> cards =
                creditCardServiceClient.getAllCards();

        return cards.stream()
                .filter(card -> {

                    BigDecimal twentyPercent =
                            card.getCreditLimit()
                                    .multiply(
                                            new BigDecimal("0.20"));

                    return card.getAvailableCredit()
                            .compareTo(twentyPercent) < 0;
                })
                .toList();
    }

    // ==========================================================
    // 15. Display customer who has spent the highest amount
    // ==========================================================

    public List<Map<String, Object>>
    getHighestSpendingCustomer() {

        List<CustomerResponse> customers =
                customerServiceClient.getAllCustomers();

        List<TransactionResponse> transactions =
                transactionServiceClient.getAllTransactions();

        Map<Long, BigDecimal> customerSpending =
                new HashMap<>();

        for (TransactionResponse transaction : transactions) {

            if ("PURCHASE".equalsIgnoreCase(
                    transaction.getTransactionType())
                    && "SUCCESS".equalsIgnoreCase(
                            transaction.getTransactionStatus())) {

                Long customerId =
                        transaction.getCustomerId();

                customerSpending.put(
                        customerId,
                        customerSpending.getOrDefault(
                                customerId,
                                BigDecimal.ZERO
                        ).add(transaction.getAmount())
                );
            }
        }

        BigDecimal highest = BigDecimal.ZERO;

        for (BigDecimal amount :
                customerSpending.values()) {

            if (amount.compareTo(highest) > 0) {
                highest = amount;
            }
        }

        List<Map<String, Object>> result =
                new java.util.ArrayList<>();

        for (CustomerResponse customer : customers) {

            BigDecimal spending =
                    customerSpending.getOrDefault(
                            customer.getCustomerId(),
                            BigDecimal.ZERO
                    );

            if (spending.compareTo(highest) == 0) {

                Map<String, Object> data =
                        new HashMap<>();

                data.put("customerId",
                        customer.getCustomerId());

                data.put("customerName",
                        customer.getCustomerName());

                data.put("spentAmount",
                        spending);

                result.add(data);
            }
        }

        return result;
    }

    // ==========================================================
    // 16. Display customer who made the highest payment
    // ==========================================================

    public List<Map<String, Object>>
    getHighestPaymentCustomer() {

        List<CustomerResponse> customers =
                customerServiceClient.getAllCustomers();

        List<TransactionResponse> transactions =
                transactionServiceClient.getAllTransactions();

        Map<Long, BigDecimal> customerPayments =
                new HashMap<>();

        for (TransactionResponse transaction : transactions) {

            if ("PAYMENT".equalsIgnoreCase(
                    transaction.getTransactionType())
                    && "SUCCESS".equalsIgnoreCase(
                            transaction.getTransactionStatus())) {

                Long customerId =
                        transaction.getCustomerId();

                customerPayments.put(
                        customerId,
                        customerPayments.getOrDefault(
                                customerId,
                                BigDecimal.ZERO
                        ).add(transaction.getAmount())
                );
            }
        }

        BigDecimal highest = BigDecimal.ZERO;

        for (BigDecimal amount :
                customerPayments.values()) {

            if (amount.compareTo(highest) > 0) {
                highest = amount;
            }
        }

        List<Map<String, Object>> result =
                new java.util.ArrayList<>();

        for (CustomerResponse customer : customers) {

            BigDecimal payment =
                    customerPayments.getOrDefault(
                            customer.getCustomerId(),
                            BigDecimal.ZERO
                    );

            if (payment.compareTo(highest) == 0) {

                Map<String, Object> data =
                        new HashMap<>();

                data.put("customerId",
                        customer.getCustomerId());

                data.put("customerName",
                        customer.getCustomerName());

                data.put("paymentAmount",
                        payment);

                result.add(data);
            }
        }

        return result;
    }

    // ==========================================================
    // 17. Display total outstanding amount
    // ==========================================================

    public BigDecimal getTotalOutstandingAmount() {

        List<CreditCardResponse> cards =
                creditCardServiceClient.getAllCards();

        BigDecimal total = BigDecimal.ZERO;

        for (CreditCardResponse card : cards) {

            total = total.add(
                    card.getOutstandingAmount());
        }

        return total;
    }

    // ==========================================================
    // 18. Display average purchase transaction amount
    // ==========================================================

    public BigDecimal getAveragePurchaseAmount() {

        List<TransactionResponse> transactions =
                transactionServiceClient.getAllTransactions();

        BigDecimal total = BigDecimal.ZERO;

        int count = 0;

        for (TransactionResponse transaction :
                transactions) {

            if ("PURCHASE".equalsIgnoreCase(
                    transaction.getTransactionType())
                    && "SUCCESS".equalsIgnoreCase(
                            transaction.getTransactionStatus())) {

                total = total.add(
                        transaction.getAmount());

                count++;
            }
        }

        if (count == 0) {
            return BigDecimal.ZERO;
        }

        return total.divide(
                BigDecimal.valueOf(count),
                2,
                RoundingMode.HALF_UP
        );
    }

    // ==========================================================
    // 19. Display largest purchase transaction
    // ==========================================================

    public TransactionResponse getLargestPurchaseTransaction() {

        List<TransactionResponse> transactions =
                transactionServiceClient.getAllTransactions();

        TransactionResponse largest = null;

        for (TransactionResponse transaction :
                transactions) {

            if ("PURCHASE".equalsIgnoreCase(
                    transaction.getTransactionType())
                    && "SUCCESS".equalsIgnoreCase(
                            transaction.getTransactionStatus())) {

                if (largest == null ||
                        transaction.getAmount()
                                .compareTo(
                                        largest.getAmount()) > 0) {

                    largest = transaction;
                }
            }
        }

        return largest;
    }

    // ==========================================================
    // 20. Display monthly spending summary of every customer
    // ==========================================================

    public List<Map<String, Object>>
    getMonthlySpendingSummary() {

        List<CustomerResponse> customers =
                customerServiceClient.getAllCustomers();

        List<TransactionResponse> transactions =
                transactionServiceClient.getAllTransactions();

        Map<Long, Map<String, BigDecimal>> data =
                new HashMap<>();

        for (TransactionResponse transaction :
                transactions) {

            if ("PURCHASE".equalsIgnoreCase(
                    transaction.getTransactionType())
                    && "SUCCESS".equalsIgnoreCase(
                            transaction.getTransactionStatus())) {

                Long customerId =
                        transaction.getCustomerId();

                String month =
                        transaction.getTransactionDate()
                                .getYear()
                                + "-"
                                + transaction.getTransactionDate()
                                        .getMonthValue();

                if (!data.containsKey(customerId)) {

                    data.put(
                            customerId,
                            new HashMap<>());
                }

                Map<String, BigDecimal> monthly =
                        data.get(customerId);

                monthly.put(
                        month,
                        monthly.getOrDefault(
                                month,
                                BigDecimal.ZERO
                        ).add(transaction.getAmount())
                );
            }
        }

        List<Map<String, Object>> result =
                new java.util.ArrayList<>();

        for (CustomerResponse customer : customers) {

            Map<String, Object> customerData =
                    new HashMap<>();

            customerData.put(
                    "customerId",
                    customer.getCustomerId());

            customerData.put(
                    "customerName",
                    customer.getCustomerName());

            customerData.put(
                    "monthlySpending",
                    data.getOrDefault(
                            customer.getCustomerId(),
                            new HashMap<>()));

            result.add(customerData);
        }

        return result;
    }
}
package com.report.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.report.dto.CreditCardResponse;
import com.report.dto.CustomerResponse;
import com.report.dto.MerchantResponse;
import com.report.dto.ReportResponse;
import com.report.dto.TransactionResponse;
import com.report.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // ============================================================
    // 1. Display all customer details
    // GET /reports/customers
    // ============================================================

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponse>>
    getAllCustomers() {

        return ResponseEntity.ok(
                reportService.getAllCustomers());
    }

    // ============================================================
    // 2. Display all credit card details
    // GET /reports/cards
    // ============================================================

    @GetMapping("/cards")
    public ResponseEntity<List<CreditCardResponse>>
    getAllCards() {

        return ResponseEntity.ok(
                reportService.getAllCards());
    }

    // ============================================================
    // 3. Display all merchant details
    // GET /reports/merchants
    // ============================================================

    @GetMapping("/merchants")
    public ResponseEntity<List<MerchantResponse>>
    getAllMerchants() {

        return ResponseEntity.ok(
                reportService.getAllMerchants());
    }

    // ============================================================
    // 4. Display complete transaction history
    // GET /reports/transactions
    // ============================================================

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>>
    getAllTransactions() {

        return ResponseEntity.ok(
                reportService.getAllTransactions());
    }

    // ============================================================
    // 5. Display customers with highest outstanding balance
    // GET /reports/customers/highest-outstanding
    // ============================================================

    @GetMapping("/customers/highest-outstanding")
    public ResponseEntity<List<Map<String, Object>>>
    getHighestOutstandingCustomers() {

        return ResponseEntity.ok(
                reportService
                        .getHighestOutstandingCustomers());
    }

    // ============================================================
    // 6. Display customers with lowest outstanding balance
    // GET /reports/customers/lowest-outstanding
    // ============================================================

    @GetMapping("/customers/lowest-outstanding")
    public ResponseEntity<List<Map<String, Object>>>
    getLowestOutstandingCustomers() {

        return ResponseEntity.ok(
                reportService
                        .getLowestOutstandingCustomers());
    }

    // ============================================================
    // 7. Display merchant with highest sales amount
    // GET /reports/merchants/highest-sales
    // ============================================================

    @GetMapping("/merchants/highest-sales")
    public ResponseEntity<List<Map<String, Object>>>
    getHighestSalesMerchant() {

        return ResponseEntity.ok(
                reportService
                        .getHighestSalesMerchant());
    }

    // ============================================================
    // 8. Display merchant with highest number of transactions
    // GET /reports/merchants/highest-transactions
    // ============================================================

    @GetMapping("/merchants/highest-transactions")
    public ResponseEntity<List<Map<String, Object>>>
    getHighestTransactionCountMerchant() {

        return ResponseEntity.ok(
                reportService
                        .getHighestTransactionCountMerchant());
    }

    // ============================================================
    // 9. Display most frequently used credit card
    // GET /reports/cards/most-used
    // ============================================================

    @GetMapping("/cards/most-used")
    public ResponseEntity<List<Map<String, Object>>>
    getMostFrequentlyUsedCard() {

        return ResponseEntity.ok(
                reportService
                        .getMostFrequentlyUsedCard());
    }

    // ============================================================
    // 10. Display least frequently used credit card
    // GET /reports/cards/least-used
    // ============================================================

    @GetMapping("/cards/least-used")
    public ResponseEntity<List<Map<String, Object>>>
    getLeastFrequentlyUsedCard() {

        return ResponseEntity.ok(
                reportService
                        .getLeastFrequentlyUsedCard());
    }

    // ============================================================
    // 11. Display total purchase amount for today
    // GET /reports/transactions/today/purchases
    // ============================================================

    @GetMapping("/transactions/today/purchases")
    public ResponseEntity<BigDecimal>
    getTodayPurchaseAmount() {

        return ResponseEntity.ok(
                reportService
                        .getTodayPurchaseAmount());
    }

    // ============================================================
    // 12. Display total payment amount for today
    // GET /reports/transactions/today/payments
    // ============================================================

    @GetMapping("/transactions/today/payments")
    public ResponseEntity<BigDecimal>
    getTodayPaymentAmount() {

        return ResponseEntity.ok(
                reportService
                        .getTodayPaymentAmount());
    }

    // ============================================================
    // 13. Display all blocked credit cards
    // GET /reports/cards/blocked
    // ============================================================

    @GetMapping("/cards/blocked")
    public ResponseEntity<List<CreditCardResponse>>
    getAllBlockedCards() {

        return ResponseEntity.ok(
                reportService
                        .getAllBlockedCards());
    }

    // ============================================================
    // 14. Display cards whose available credit is
    //     below 20% of credit limit
    // GET /reports/cards/low-credit
    // ============================================================

    @GetMapping("/cards/low-credit")
    public ResponseEntity<List<CreditCardResponse>>
    getCardsBelow20PercentCredit() {

        return ResponseEntity.ok(
                reportService
                        .getCardsBelow20PercentCredit());
    }

    // ============================================================
    // 15. Display customer who has spent highest amount
    // GET /reports/customers/highest-spending
    // ============================================================

    @GetMapping("/customers/highest-spending")
    public ResponseEntity<List<Map<String, Object>>>
    getHighestSpendingCustomer() {

        return ResponseEntity.ok(
                reportService
                        .getHighestSpendingCustomer());
    }

    // ============================================================
    // 16. Display customer who made highest payment
    // GET /reports/customers/highest-payment
    // ============================================================

    @GetMapping("/customers/highest-payment")
    public ResponseEntity<List<Map<String, Object>>>
    getHighestPaymentCustomer() {

        return ResponseEntity.ok(
                reportService
                        .getHighestPaymentCustomer());
    }

    // ============================================================
    // 17. Display total outstanding amount
    // GET /reports/customers/total-outstanding
    // ============================================================

    @GetMapping("/customers/total-outstanding")
    public ResponseEntity<BigDecimal>
    getTotalOutstandingAmount() {

        return ResponseEntity.ok(
                reportService
                        .getTotalOutstandingAmount());
    }

    // ============================================================
    // 18. Display average purchase transaction amount
    // GET /reports/transactions/average-purchase
    // ============================================================

    @GetMapping("/transactions/average-purchase")
    public ResponseEntity<BigDecimal>
    getAveragePurchaseAmount() {

        return ResponseEntity.ok(
                reportService
                        .getAveragePurchaseAmount());
    }

    // ============================================================
    // 19. Display largest purchase transaction
    // GET /reports/transactions/largest-purchase
    // ============================================================

    @GetMapping("/transactions/largest-purchase")
    public ResponseEntity<TransactionResponse>
    getLargestPurchaseTransaction() {

        TransactionResponse response =
                reportService
                        .getLargestPurchaseTransaction();

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // 20. Display monthly spending summary
    // GET /reports/customers/monthly-spending
    // ============================================================

    @GetMapping("/customers/monthly-spending")
    public ResponseEntity<List<Map<String, Object>>>
    getMonthlySpendingSummary() {

        return ResponseEntity.ok(
                reportService
                        .getMonthlySpendingSummary());
    }
}
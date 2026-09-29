package com.creditcard.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.creditcard.dto.MerchantRequest;
import com.creditcard.dto.MerchantResponse;
import com.creditcard.service.MerchantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping
    public ResponseEntity<MerchantResponse> addMerchant(
            @Valid @RequestBody MerchantRequest request) {

        MerchantResponse created =
                merchantService.addMerchant(request);

        return ResponseEntity
                .created(
                    URI.create(
                        "/merchants/" + created.merchantId()
                    )
                )
                .body(created);
    }

    @GetMapping("/{merchantId}")
    public ResponseEntity<MerchantResponse> getMerchant(
            @PathVariable Long merchantId) {

        return ResponseEntity.ok(
                merchantService.getMerchant(merchantId)
        );
    }

    @GetMapping
    public ResponseEntity<List<MerchantResponse>> getAllMerchants() {

        return ResponseEntity.ok(
                merchantService.getAllMerchants()
        );
    }

    @PutMapping("/{merchantId}")
    public ResponseEntity<MerchantResponse> updateMerchant(
            @PathVariable Long merchantId,
            @Valid @RequestBody MerchantRequest request) {

        return ResponseEntity.ok(
                merchantService.updateMerchant(
                        merchantId,
                        request
                )
        );
    }

    @DeleteMapping("/{merchantId}")
    public ResponseEntity<Void> deleteMerchant(
            @PathVariable Long merchantId) {

        merchantService.deleteMerchant(merchantId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<MerchantResponse>> searchMerchants(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                merchantService.searchMerchants(keyword)
        );
    }
}
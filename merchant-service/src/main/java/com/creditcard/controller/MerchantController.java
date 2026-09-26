package com.creditcard.controller;

import com.creditcard.dto.MerchantRequest;
import com.creditcard.entity.Merchant;
import com.creditcard.service.MerchantService;
import jakarta.validation.Valid;
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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }
    

    @PostMapping
    public ResponseEntity<Merchant> addMerchant(
            @Valid @RequestBody MerchantRequest request
    ) {
        Merchant created = merchantService.addMerchant(request);

        return ResponseEntity
                .created(URI.create("/merchants/" + created.getMerchantId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public Merchant getMerchant(@PathVariable Long id) {
        return merchantService.getMerchant(id);
    }

    @GetMapping
    public List<Merchant> getAllMerchants() {
        return merchantService.getAllMerchants();
    }

    @PutMapping("/{id}")
    public Merchant updateMerchant(
            @PathVariable Long id,
            @Valid @RequestBody MerchantRequest request
    ) {
        return merchantService.updateMerchant(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) {
        merchantService.deleteMerchant(id);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/search")
    public List<Merchant> searchMerchants(@RequestParam String keyword) {
        return merchantService.searchMerchants(keyword);
    }
}
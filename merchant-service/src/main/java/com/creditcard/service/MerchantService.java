package com.creditcard.service;

import com.creditcard.dto.MerchantRequest;
import com.creditcard.entity.Merchant;

import java.util.List;

public interface MerchantService {

    Merchant addMerchant(MerchantRequest request);

    Merchant getMerchant(Long id);

    List<Merchant> getAllMerchants();

    Merchant updateMerchant(Long id, MerchantRequest request);

    void deleteMerchant(Long id);

    List<Merchant> searchMerchants(String keyword);
}
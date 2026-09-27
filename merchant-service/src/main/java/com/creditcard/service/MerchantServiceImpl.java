package com.creditcard.service;

import com.creditcard.dto.MerchantRequest;
import com.creditcard.entity.Merchant;
import com.creditcard.exception.MerchantNotFoundException;
import com.creditcard.repository.MerchantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantServiceImpl(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @Override
    @Transactional
    public Merchant addMerchant(MerchantRequest request) {
        Merchant merchant = new Merchant();
        copyFields(request, merchant);
        return merchantRepository.save(merchant);
    }

    @Override
    @Transactional(readOnly = true)
    public Merchant getMerchant(Long id) {
        return merchantRepository.findById(id)
                .orElseThrow(() -> new MerchantNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }

    @Override
    @Transactional
    public Merchant updateMerchant(Long id, MerchantRequest request) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new MerchantNotFoundException(id));

        copyFields(request, merchant);
        return merchantRepository.save(merchant);
    }

    @Override
    @Transactional
    public void deleteMerchant(Long id) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new MerchantNotFoundException(id));

        merchantRepository.delete(merchant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Merchant> searchMerchants(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "search keyword cannot be blank"
            );
        }

        String term = keyword.trim();

        return merchantRepository
                .findByMerchantNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrLocationContainingIgnoreCase(
                        term, term, term
                );
    }

    private void copyFields(MerchantRequest request, Merchant merchant) {
        merchant.setMerchantName(request.merchantName().trim());
        merchant.setCategory(request.category().trim());
        merchant.setLocation(request.location().trim());
    }
}
package com.creditcard.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.creditcard.dto.MerchantRequest;
import com.creditcard.dto.MerchantResponse;
import com.creditcard.entity.Merchant;
import com.creditcard.exception.MerchantNotFoundException;
import com.creditcard.repository.MerchantRepository;

@Service
@Transactional
public class MerchantService {

	private final MerchantRepository merchantRepository;

	public MerchantService(MerchantRepository merchantRepository) {
		this.merchantRepository = merchantRepository;
	}

	public MerchantResponse addMerchant(MerchantRequest request) {

		Merchant merchant = new Merchant();

		copyFields(request, merchant);

		Merchant savedMerchant = merchantRepository.save(merchant);

		return mapToResponse(savedMerchant);
	}

	@Transactional(readOnly = true)
	public MerchantResponse getMerchant(Long merchantId) {

		Merchant merchant = merchantRepository.findById(merchantId)
				.orElseThrow(() -> new MerchantNotFoundException(merchantId));

		return mapToResponse(merchant);
	}

	@Transactional(readOnly = true)
	public List<MerchantResponse> getAllMerchants() {

		return merchantRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	public MerchantResponse updateMerchant(Long merchantId, MerchantRequest request) {

		Merchant merchant = merchantRepository.findById(merchantId)
				.orElseThrow(() -> new MerchantNotFoundException(merchantId));

		copyFields(request, merchant);

		Merchant updatedMerchant = merchantRepository.save(merchant);

		return mapToResponse(updatedMerchant);
	}

	public void deleteMerchant(Long merchantId) {

		Merchant merchant = merchantRepository.findById(merchantId)
				.orElseThrow(() -> new MerchantNotFoundException(merchantId));

		merchantRepository.delete(merchant);
	}

	@Transactional(readOnly = true)
	public List<MerchantResponse> searchMerchants(String keyword) {

		if (keyword == null || keyword.isBlank()) {
			throw new IllegalArgumentException("Search keyword cannot be blank");
		}

		String term = keyword.trim();

		return merchantRepository
				.findByMerchantNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrLocationContainingIgnoreCase(
						term, term, term)
				.stream().map(this::mapToResponse).toList();
	}

	private void copyFields(MerchantRequest request, Merchant merchant) {

		merchant.setMerchantName(request.merchantName().trim());

		merchant.setCategory(request.category().trim());

		merchant.setLocation(request.location().trim());
	}

	private MerchantResponse mapToResponse(Merchant merchant) {

		return new MerchantResponse(merchant.getMerchantId(), merchant.getMerchantName(), merchant.getCategory(),
				merchant.getLocation());
	}
}
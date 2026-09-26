package com.creditcard.repository;

import com.creditcard.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    List<Merchant>
    findByMerchantNameContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrLocationContainingIgnoreCase(
            String merchantName,
            String category,
            String location
    );
}
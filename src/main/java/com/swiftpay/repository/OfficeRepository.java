package com.swiftpay.repository;

import com.swiftpay.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, Long> {
    Optional<Office> findByCodeISO(String codeISO);
}

package com.swiftpay.repository;

import com.swiftpay.model.Agency;
import com.swiftpay.model.Transfer;
import com.swiftpay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Integer> {
    Optional<Transfer> findByTransferMTCN(String transferMTCN);

    List<Transfer> findAllByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Transfer> findAllByCreatedAtBetweenAndCreatedBy(LocalDateTime startDate, LocalDateTime endDate, User createdBy);

    List<Transfer> findAllByCreatedBy(User createdBy);

    List<Transfer> findAllByCreatedAtBetweenAndCreatedBy_Agency(LocalDateTime startDate, LocalDateTime endDate, Agency agency);

    List<Transfer> findAllByCreatedBy_Agency(Agency agency);

    List<Transfer> findByStatusIgnoreCaseAndCreatedAtBetween(String status, LocalDateTime startDate, LocalDateTime endDate);

}

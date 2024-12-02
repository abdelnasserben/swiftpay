package com.swiftpay.mapper;

import com.swiftpay.dto.TransferDto;
import com.swiftpay.model.Transfer;

public class TransferMapper {

    public static TransferDto toDto(Transfer transfer) {
        if (transfer == null) {
            return null;
        }

        TransferDto dto = new TransferDto();
        dto.setId(transfer.getId());
        dto.setTransferMTCN(transfer.getTransferMTCN());
        dto.setSender(CustomerMapper.toDto(transfer.getSender()));
        dto.setReceiver(CustomerMapper.toDto(transfer.getReceiver()));
        dto.setAmountSent(transfer.getAmountSent());
        dto.setCurrencySent(transfer.getCurrencySent());
        dto.setFees(transfer.getFees());
        dto.setRate(transfer.getRate());
        dto.setTotalAmount(transfer.getTotalAmount());
        dto.setAmountToPay(transfer.getAmountToPay());
        dto.setCurrencyToPay(transfer.getCurrencyToPay());
        dto.setIssuingCountry(transfer.getIssuingCountry());
        dto.setReceivingCountry(transfer.getReceivingCountry());
        dto.setStatus(transfer.getStatus());
        dto.setCreatedAt(transfer.getCreatedAt());
        dto.setUpdatedAt(transfer.getUpdatedAt());

        return dto;
    }

    public static Transfer toEntity(TransferDto dto) {
        if (dto == null) {
            return null;
        }

        Transfer transfer = new Transfer();
        transfer.setId(dto.getId());
        transfer.setTransferMTCN(dto.getTransferMTCN());
        transfer.setSender(CustomerMapper.toEntity(dto.getSender()));
        transfer.setReceiver(CustomerMapper.toEntity(dto.getReceiver()));
        transfer.setAmountSent(dto.getAmountSent());
        transfer.setCurrencySent(dto.getCurrencySent());
        transfer.setFees(dto.getFees());
        transfer.setRate(dto.getRate());
        transfer.setTotalAmount(dto.getTotalAmount());
        transfer.setAmountToPay(dto.getAmountToPay());
        transfer.setCurrencyToPay(dto.getCurrencyToPay());
        transfer.setIssuingCountry(dto.getIssuingCountry());
        transfer.setReceivingCountry(dto.getReceivingCountry());
        transfer.setStatus(dto.getStatus());
        transfer.setCreatedAt(dto.getCreatedAt());
        transfer.setUpdatedAt(dto.getUpdatedAt());

        return transfer;
    }
}


package com.swiftpay.dto;

import java.time.LocalDateTime;

public class TransferDto {
    private Long id;
    private String transferMTCN;
    private CustomerDto sender;
    private CustomerDto receiver;
    private double amountSent;
    private double amountToPay;
    private String currencySent;
    private String currencyToPay;
    private double fees;
    private double rate;
    private double totalAmount;
    private String IssuingCountry;
    private String receivingCountry;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransferMTCN() {
        return transferMTCN;
    }

    public void setTransferMTCN(String transferMTCN) {
        this.transferMTCN = transferMTCN;
    }

    public CustomerDto getSender() {
        return sender;
    }

    public void setSender(CustomerDto sender) {
        this.sender = sender;
    }

    public CustomerDto getReceiver() {
        return receiver;
    }

    public void setReceiver(CustomerDto receiver) {
        this.receiver = receiver;
    }

    public double getAmountSent() {
        return amountSent;
    }

    public void setAmountSent(double amountSent) {
        this.amountSent = amountSent;
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public String getCurrencySent() {
        return currencySent;
    }

    public void setCurrencySent(String currencySent) {
        this.currencySent = currencySent;
    }

    public String getCurrencyToPay() {
        return currencyToPay;
    }

    public void setCurrencyToPay(String currencyToPay) {
        this.currencyToPay = currencyToPay;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getIssuingCountry() {
        return IssuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        IssuingCountry = issuingCountry;
    }

    public String getReceivingCountry() {
        return receivingCountry;
    }

    public void setReceivingCountry(String receivingCountry) {
        this.receivingCountry = receivingCountry;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}


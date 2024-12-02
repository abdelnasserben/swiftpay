package com.swiftpay.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OfficeDto {

    private Long id;
    private String codeISO;
    private String name;
    private String currency;
    private double interestRate;
    private List<CurrencyDto> currencies = new ArrayList<>();
    private List<AgencyDto> agencies;

    public String getCurrenciesAsString() {
        return currencies.stream()
                .map(CurrencyDto::getCode)
                .collect(Collectors.joining(", "));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeISO() {
        return codeISO;
    }

    public void setCodeISO(String codeISO) {
        this.codeISO = codeISO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public List<CurrencyDto> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<CurrencyDto> currencies) {
        this.currencies = currencies;
    }

    public List<AgencyDto> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<AgencyDto> agencies) {
        this.agencies = agencies;
    }
}



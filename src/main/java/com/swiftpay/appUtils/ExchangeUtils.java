package com.swiftpay.appUtils;

import com.swiftpay.dto.ExchangeResponse;

public class ExchangeUtils {

    private static ExchangeResponse exchangeResponse;
    private static double interestRate;
    private static String countryOrigin;


    public static void setExchangeResponse(ExchangeResponse exchangeResponse) {
        ExchangeUtils.exchangeResponse = exchangeResponse;
    }

    public static double getInterestRate() {
        return interestRate;
    }

    public static void setInterestRate(double interestRate) {
        ExchangeUtils.interestRate = interestRate;
    }

    public static String getBaseCurrency() {
        return exchangeResponse.getBase_code();
    }

    public static double getCurrencyRateFor(String targetCurrency) {
        return exchangeResponse.getConversion_rates().get(targetCurrency);
    }

    public static String getCountryOrigin() {
        return countryOrigin;
    }

    public static void setCountryOrigin(String countryOrigin) {
        ExchangeUtils.countryOrigin = countryOrigin;
    }
}

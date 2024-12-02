package com.swiftpay.exception;

public class FailedRetrieveExchangeRateException extends RuntimeException {
    public FailedRetrieveExchangeRateException(String targetCurrency) {
        super("Une erreur s'est produite lors de la récupération du taux de change " + targetCurrency);
    }
}

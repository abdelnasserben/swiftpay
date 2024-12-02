package com.swiftpay.controller;

import com.swiftpay.appUtils.ExchangeUtils;
import com.swiftpay.appUtils.Helper;
import com.swiftpay.dto.CustomerDto;
import com.swiftpay.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final CustomerService customerService;

    public ApiController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/send-money")
    private ResponseEntity<Map<String, Object>> calculateSendingAmounts(
            @RequestParam(name = "receiverCountry", defaultValue = "") String receiverCountry,
            @RequestParam(name = "amountSent", defaultValue = "") String amountSentStr,
            @RequestParam(name = "totalAmount", defaultValue = "") String totalAmountStr) {

        double amountToSent = !amountSentStr.isEmpty() ? Double.parseDouble(amountSentStr) : 0.0;
        double totalAmount = !totalAmountStr.isEmpty() ? Double.parseDouble(totalAmountStr) : 0.0;
        double amountToPay;
        double fees = 0;

        String[] extractedCountryAndCurrencyArray = Helper.extractCountryAndCurrencyArray(receiverCountry);
        String receiverCountryName = extractedCountryAndCurrencyArray[0];
        String currencyToPay = extractedCountryAndCurrencyArray[1];


        double interestRate = ExchangeUtils.getInterestRate();

        if (amountToSent > 0) {
            fees = amountToSent * interestRate;
            totalAmount = amountToSent + fees;
        } else if (totalAmount > 0) {
            amountToSent = totalAmount / (1 + interestRate);
            fees = totalAmount - amountToSent;
        }

        String baseCurrency = ExchangeUtils.getBaseCurrency();
        double rate = ExchangeUtils.getCurrencyRateFor(currencyToPay);
        amountToPay = amountToSent * rate;

        Map<String, Object> response = new HashMap<>();
        response.put("receiverCountryName", receiverCountryName);
        response.put("baseCurrency", baseCurrency);
        response.put("amountSent", Helper.formatToTwoDecimalPlaces(amountToSent));
        response.put("fees", Helper.formatToTwoDecimalPlaces(fees));
        response.put("totalAmount", Helper.formatToTwoDecimalPlaces(totalAmount));
        response.put("amountToPay", Helper.formatToTwoDecimalPlaces(amountToPay));
        response.put("currencyToPay", currencyToPay);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer")
    public ResponseEntity<CustomerDto> getCustomer(@RequestParam String identityType, @RequestParam String identityNumber) {
        CustomerDto customer = customerService.getByIDTypeAndNumber(identityType, identityNumber);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }

        return ResponseEntity.notFound().build();
    }
}

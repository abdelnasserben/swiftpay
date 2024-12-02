package com.swiftpay.appUtils;

import com.swiftpay.enums.Country;

import java.util.Random;

public class Helper {

    public static String generate10Digits() {
        Random random = new Random();
        int part1 = random.nextInt(100);
        int part2 = random.nextInt(10000);
        int part3 = random.nextInt(1000, 10000);

        return String.format("%02d%04d%d", part1, part2, part3);
    }

    public static String[] extractCountryAndCurrencyArray(String text) {

        String[] parts = text.split("_");
        String countryCode = parts[0];
        String currencyCode = parts[1];

        return new String[]{Country.fromCode(countryCode).getName(), currencyCode};
    }

    public static double formatToTwoDecimalPlaces(double value) {
        return Math.floor(value * 100) / 100;
    }
}

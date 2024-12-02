package com.swiftpay.appUtils;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatterUtils {
    public static String formatMTCN(String code) {
        if (code != null && code.length() == 10) {
            return code.substring(0, 3) + "-" + code.substring(3, 6) + "-" + code.substring(6);
        }
        return code;
    }

    public static String formatAmount(double amount) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.FRANCE);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(amount);
    }
}


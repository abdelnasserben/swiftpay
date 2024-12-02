package com.swiftpay.appUtils;

import org.springframework.ui.Model;

public interface PageAttributesProvider {

    default void configurePageAttributes(Model model) {
        model.addAttribute("pageTitle", getPageTitle());
        model.addAttribute("currentPage", getPageUrl());
    }

    String getPageTitle();

    String getPageUrl();
}

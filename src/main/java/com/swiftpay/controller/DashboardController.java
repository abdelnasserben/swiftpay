package com.swiftpay.controller;

import com.swiftpay.appUtils.Endpoint;
import com.swiftpay.appUtils.ExchangeUtils;
import com.swiftpay.appUtils.PageAttributesProvider;
import com.swiftpay.dto.OfficeDto;
import com.swiftpay.service.OfficeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController implements PageAttributesProvider {

    private final OfficeService officeService;

    public DashboardController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping(value = Endpoint.DASHBOARD)
    public String dashboard(Model model) {

        Map<String, Double> topCountries = officeService.getAllOffices().stream()
                .limit(10)
                .collect(Collectors.toMap(
                        OfficeDto::getName,
                        office -> ExchangeUtils.getCurrencyRateFor(office.getCurrency())
                ));

        model.addAttribute("topCountries", topCountries);
        configurePageAttributes(model);
        return "dashboard";
    }

    @Override
    public String getPageTitle() {
        return "Tableau de bord";
    }

    @Override
    public String getPageUrl() {
        return Endpoint.DASHBOARD;
    }
}

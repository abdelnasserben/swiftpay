package com.swiftpay.controller;

import com.swiftpay.appUtils.Endpoint;
import com.swiftpay.appUtils.PageAttributesProvider;
import com.swiftpay.dto.OfficeDto;
import com.swiftpay.dto.SendDto;
import com.swiftpay.enums.AlertMessageTag;
import com.swiftpay.enums.Country;
import com.swiftpay.service.OfficeService;
import com.swiftpay.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
public class SendController implements PageAttributesProvider {

    private final TransferService transferService;
    private final OfficeService officeService;
    private final static String SEND_VIEW = "send";

    public SendController(TransferService transferService, OfficeService officeService) {
        this.transferService = transferService;
        this.officeService = officeService;
    }

    @GetMapping(value = Endpoint.SEND)
    public String sendMoney(Model model) {

        model.addAttribute("sendDto", new SendDto());
        return loadSendingView(model);
    }

    @PostMapping(value = Endpoint.SEND)
    public String submitSendMoney(Model model, @Valid SendDto sendDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            model.addAttribute(AlertMessageTag.ERROR.toString(), "Invalid Information.");
            return loadSendingView(model);
        }

        String transferMTCN = transferService.sendMoney(sendDto);
        redirectAttributes.addFlashAttribute(AlertMessageTag.SUCCESS.toString(), "Transfer sent successfully");

        return String.format("redirect:%s/%s", Endpoint.TRANSFERS, transferMTCN);
    }

    private String loadSendingView(Model model) {
        List<OfficeDto> sortedOffices = officeService.getAllOffices().stream()
                .sorted(Comparator.comparing(OfficeDto::getName))
                .toList();

        Country[] countries = Country.values();
        Arrays.sort(countries, Comparator.comparing(Country::getName));

        model.addAttribute("offices", sortedOffices);
        model.addAttribute("countries", countries);
        configurePageAttributes(model);
        return SEND_VIEW;
    }

    @Override
    public String getPageTitle() {
        return "Envoyez de l'argent";
    }

    @Override
    public String getPageUrl() {
        return Endpoint.SEND;
    }
}

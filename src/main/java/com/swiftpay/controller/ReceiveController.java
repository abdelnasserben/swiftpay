package com.swiftpay.controller;

import com.swiftpay.appUtils.Endpoint;
import com.swiftpay.appUtils.PageAttributesProvider;
import com.swiftpay.dto.CustomerDto;
import com.swiftpay.dto.TransferDto;
import com.swiftpay.enums.AlertMessageTag;
import com.swiftpay.enums.Country;
import com.swiftpay.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReceiveController implements PageAttributesProvider {

    private final TransferService transferService;
    private final static String PAYOUT_VIEW = "payout";

    public ReceiveController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping(value = Endpoint.RECEIVE)
    public String displayReceivePage(Model model) {
        model.addAttribute("actionType", "receive");
        configurePageAttributes(model);
        return "receive";
    }

    @PostMapping(value = Endpoint.PAYOUT)
    public String displayPayoutForm(@RequestParam String transferMTCN, Model model) {

        CustomerDto customerDto = getTransferAndConfigPayoutAttributes(model, transferMTCN).getReceiver();
        model.addAttribute("customerDto", customerDto);
        return PAYOUT_VIEW;
    }

    @PatchMapping(value = Endpoint.PAYOUT)
    public String processPayout(@Valid CustomerDto customerDto,
                                BindingResult bindingResult,
                                @RequestParam String transferMTCN,
                                Model model, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            getTransferAndConfigPayoutAttributes(model, transferMTCN);
            model.addAttribute(AlertMessageTag.ERROR.toString(), "Invalid Information.");
            return PAYOUT_VIEW;
        }

        transferService.pay(transferMTCN, customerDto);
        redirectAttributes.addFlashAttribute(AlertMessageTag.SUCCESS.toString(), "Transfer Successfully paid");
        return String.format("redirect:%s/%s", Endpoint.TRANSFERS, transferMTCN);
    }

    private TransferDto getTransferAndConfigPayoutAttributes(Model model, String transferMTCN) {
        TransferDto transfer = transferService.getByMTCN(transferMTCN);
        model.addAttribute("transfer", transfer);
        model.addAttribute("countries", Country.values());
        return transfer;
    }

    @Override
    public String getPageTitle() {
        return "Recevoir de l'argent";
    }

    @Override
    public String getPageUrl() {
        return Endpoint.RECEIVE;
    }
}

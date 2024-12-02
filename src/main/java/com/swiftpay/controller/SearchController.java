package com.swiftpay.controller;

import com.swiftpay.appUtils.Endpoint;
import com.swiftpay.appUtils.PageAttributesProvider;
import com.swiftpay.dto.TransferDto;
import com.swiftpay.service.TransferService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SearchController implements PageAttributesProvider {

    private final TransferService transferService;

    public SearchController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping(value = Endpoint.SEARCH)
    public String displaySearchPage(Model model) {
        configurePageAttributes(model);
        return "search";
    }

    @PostMapping(value = Endpoint.SEARCH)
    public String processSearchRequest(@RequestParam String transferMTCN,
                                       @RequestParam(name = "actionType", defaultValue = "", required = false) String actionType,
                                       HttpServletRequest request,
                                       RedirectAttributes redirect) {

        if (transferMTCN == null || transferMTCN.trim().isEmpty()) {
            redirect.addFlashAttribute("transferMTCNError", "Invalid Information.");
        } else {
            TransferDto transfer = transferService.getByMTCN(transferMTCN);

            redirect.addFlashAttribute("isPayable", actionType.equalsIgnoreCase("receive") && TransferService.isPayable(transfer));
            redirect.addFlashAttribute("transfer", transfer);
        }

        return "redirect:" + request.getHeader("Referer");
    }

    @Override
    public String getPageTitle() {
        return "Rechercher un transfert";
    }

    @Override
    public String getPageUrl() {
        return Endpoint.SEARCH;
    }
}

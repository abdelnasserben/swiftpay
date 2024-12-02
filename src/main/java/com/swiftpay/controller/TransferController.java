package com.swiftpay.controller;

import com.swiftpay.appUtils.Endpoint;
import com.swiftpay.appUtils.PageAttributesProvider;
import com.swiftpay.dto.TransferDto;
import com.swiftpay.enums.AlertMessageTag;
import com.swiftpay.service.TransferService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value = Endpoint.TRANSFERS)
public class TransferController implements PageAttributesProvider {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping
    public String listAllTransfers(Model model) {

        model.addAttribute("transfers", transferService.findUserTransfers(null, null));
        configurePageAttributes(model);
        return "transfers";
    }

    @PostMapping
    public String filterTransfers(
            @RequestParam(required = false, defaultValue = "") LocalDate startDate,
            @RequestParam(required = false, defaultValue = "") LocalDate endDate,
            @RequestParam(required = false) String filterType,
            Model model) {

        List<TransferDto> transfers;

        if (filterType.equals("byAgency")) {
            transfers = transferService.findAgencyTransfers(startDate, endDate);
        } else {
            transfers = transferService.findUserTransfers(startDate, endDate);
        }

        model.addAttribute("filterType", filterType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("transfers", transfers);
        configurePageAttributes(model);
        return "transfers";
    }

    @GetMapping("/{transferMTCN}")
    public String showTransferDetails(Model model, @PathVariable String transferMTCN) {

        model.addAttribute("transfer", transferService.getByMTCN(transferMTCN));
        configurePageAttributes(model);
        return "transfers-details";
    }

    @PreAuthorize("hasRole('ADMIN', 'BO')")
    @PostMapping("/{transferMTCN}/refund")
    public String handleTransferRefund(@PathVariable String transferMTCN, RedirectAttributes redirectAttributes) {

        transferService.refund(transferMTCN);
        return redirectToTransferDetails(transferMTCN, redirectAttributes, "Transfer refunded successfully!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{transferMTCN}/approve")
    public String handleTransferApprobation(@PathVariable String transferMTCN, RedirectAttributes redirectAttributes) {

        transferService.approve(transferMTCN);
        return redirectToTransferDetails(transferMTCN, redirectAttributes, "Transfer approved successfully!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{transferMTCN}/lock")
    public String handleTransferLocking(@PathVariable String transferMTCN, RedirectAttributes redirectAttributes) {

        transferService.lock(transferMTCN);
        return redirectToTransferDetails(transferMTCN, redirectAttributes, "Transfer locked successfully!");
    }

    private static String redirectToTransferDetails(String transferMTCN, RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute(AlertMessageTag.SUCCESS.toString(), message);
        return "redirect:" + Endpoint.TRANSFERS + "/" + transferMTCN;
    }


    @Override
    public String getPageTitle() {
        return "Liste de Transferts";
    }

    @Override
    public String getPageUrl() {
        return Endpoint.TRANSFERS;
    }
}

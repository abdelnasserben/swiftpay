package com.swiftpay.controller;

import com.swiftpay.appUtils.Endpoint;
import com.swiftpay.appUtils.PageAttributesProvider;
import com.swiftpay.dto.AgencyDto;
import com.swiftpay.dto.OfficeDto;
import com.swiftpay.dto.TransferDto;
import com.swiftpay.dto.UserDto;
import com.swiftpay.enums.AlertMessageTag;
import com.swiftpay.enums.Country;
import com.swiftpay.service.OfficeService;
import com.swiftpay.service.TransferService;
import com.swiftpay.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController implements PageAttributesProvider {

    private final UserService userService;
    private final TransferService transferService;
    private final OfficeService officeService;
    private final static String OFFICES_VIEW_NAM = "admin-offices";
    public static final String TRANSFERS_VIEW_NAME = "admin-transfers";

    public AdminController(OfficeService officeService, UserService userService, TransferService transferService) {
        this.officeService = officeService;
        this.userService = userService;
        this.transferService = transferService;
    }

    @GetMapping(value = Endpoint.ADMIN)
    public String showAdminDashboard(Model model) {

        configurePageAttributes(model);
        return "admin";
    }

    /** FOR OFFICES **/

    @GetMapping(value = Endpoint.ADMIN_OFFICES)
    public String listAllOffices(Model model) {

        configurePageAttributes(model);
        model.addAttribute("offices", officeService.getAllOffices());
        model.addAttribute("countries", Country.values());
        return OFFICES_VIEW_NAM;
    }

    @PostMapping(value = Endpoint.ADMIN_OFFICES)
    public String handleOfficeCreation(@RequestParam(name = "codeISO", defaultValue = "") String codeISO,
                                       @RequestParam(name = "interestRate", defaultValue = "0.0") double interestRate,
                                       @RequestParam(name = "usdCurrency", defaultValue = "false") boolean usdCurrency,
                                       @RequestParam(name = "eurCurrency", defaultValue = "false") boolean eurCurrency,
                                       Model model, RedirectAttributes redirectAttributes) {

        if (codeISO.isEmpty() || interestRate < 1) {
            configurePageAttributes(model);
            model.addAttribute("offices", officeService.getAllOffices());
            model.addAttribute("countries", Country.values());
            model.addAttribute(AlertMessageTag.ERROR.toString(), "Invalid Information!");
            return OFFICES_VIEW_NAM;
        }

        officeService.createNewOffice(codeISO, interestRate, usdCurrency, eurCurrency);
        redirectAttributes.addFlashAttribute(AlertMessageTag.SUCCESS.toString(), "New office added successfully!");
        return "redirect:" + Endpoint.ADMIN_OFFICES;
    }

    /** FOR AGENCIES **/

    @GetMapping(value = Endpoint.ADMIN_AGENCIES)
    public String listAllAgencies(Model model) {
        return loadAgencyView(model, officeService.getAllAgencies(), true, officeService.getAllOffices());
    }

    @GetMapping(value = Endpoint.ADMIN_AGENCIES + "/{codeISO}")
    public String listAgenciesOfOffice(Model model, @PathVariable String codeISO) {
        OfficeDto office = officeService.getOfficeByCodeIso(codeISO);
        model.addAttribute("office", office);
        return loadAgencyView(model, office.getAgencies(), false, List.of(office));
    }

    @PostMapping(value = Endpoint.ADMIN_AGENCIES)
    public String handleAgencyCreation(@RequestParam(name = "codeISO", defaultValue = "") String codeISO,
                                       @RequestParam(name = "agencyName", defaultValue = "") String agencyName,
                                       @RequestParam(name = "agencyAddress", defaultValue = "") String agencyAddress,
                                       HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (!codeISO.isEmpty() && !agencyName.isEmpty() && !agencyAddress.isEmpty()) {
            redirectAttributes.addFlashAttribute(AlertMessageTag.SUCCESS.toString(), "New agency added successfully!");
            officeService.createNewAgency(codeISO, agencyName, agencyAddress);
        } else {
            redirectAttributes.addFlashAttribute(AlertMessageTag.ERROR.toString(), "Invalid Information!");
        }

        return "redirect:" + request.getHeader("Referer");
    }

    /**
     * Configures and returns the "agencies" view with required model attributes.
     *
     * @param model          The model to pass attributes to the view.
     * @param isAgenciesList Flag indicating if it's a list of agencies view (true) or a specific office's agencies view (false).
     * @param offices        The list of offices, used for displaying or creating new agencies.
     * @return The name of the view ("agencies").
     */
    private String loadAgencyView(Model model, List<AgencyDto> agencies, boolean isAgenciesList, List<OfficeDto> offices) {
        configurePageAttributes(model);
        model.addAttribute("agencies", agencies);
        model.addAttribute("isAgenciesList", isAgenciesList);
        model.addAttribute("offices", offices); //for creation of a new agency
        return "admin-agencies";
    }

    /** FOR USERS **/

    @GetMapping(value = Endpoint.ADMIN_USERS)
    public String listAllUsers(Model model) {
        List<AgencyDto> agencies = officeService.getAllAgencies();
        return loadUserView(model, userService.getAllUsers(), true, agencies);
    }

    @GetMapping(value = Endpoint.ADMIN_USERS + "/{id}")
    public String listUsersOfAgency(Model model, @PathVariable Long id) {
        AgencyDto agency = officeService.getAgencyById(id);
        model.addAttribute("agency", agency);
        return loadUserView(model, agency.getUsers(), false, List.of(agency));
    }

    @PostMapping(value = Endpoint.ADMIN_USERS)
    public String handleUserCreation(@RequestParam(name = "agencyId", defaultValue = "0") Long agencyId,
                                     @RequestParam(name = "username", defaultValue = "") String username,
                                     @RequestParam(name = "password", defaultValue = "") String password,
                                     @RequestParam(name = "role", defaultValue = "") String role,
                                     HttpServletRequest request, RedirectAttributes redirectAttributes) {

        if (agencyId > 0 && !username.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
            redirectAttributes.addFlashAttribute(AlertMessageTag.SUCCESS.toString(), "New user added successfully!");
            userService.createUser(agencyId, username, password, role);
        } else {
            redirectAttributes.addFlashAttribute(AlertMessageTag.ERROR.toString(), "Invalid Information!");
        }

        return "redirect:" + request.getHeader("Referer");
    }

    private static String loadUserView(Model model, List<UserDto> users, boolean isUsersList, List<AgencyDto> agencies) {
        model.addAttribute("users", users);
        model.addAttribute("isUsersList", isUsersList);
        model.addAttribute("agencies", agencies); //for creation of a new user
        return "users";
    }

    /** FOR TRANSFERS **/

    @GetMapping(value = Endpoint.ADMIN_TRANSFERS)
    public String listAllTransfers(Model model) {
        model.addAttribute("transfers", transferService.findAll(null, null));
        configurePageAttributes(model);
        return TRANSFERS_VIEW_NAME;
    }

    @PostMapping(value = Endpoint.ADMIN_TRANSFERS)
    public String filterTransfers(
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "") LocalDate startDate,
            @RequestParam(required = false, defaultValue = "") LocalDate endDate,
            Model model) {

        List<TransferDto> transfers;

        if (status != null && !status.equals("all")) {
            transfers = transferService.filterTransfers(status, startDate, endDate);
        } else {
            transfers = transferService.filterTransfers(null, startDate, endDate);
        }

        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("transfers", transfers);
        configurePageAttributes(model);
        return TRANSFERS_VIEW_NAME;
    }

    @Override
    public String getPageTitle() {
        return "Administration";
    }

    @Override
    public String getPageUrl() {
        return Endpoint.ADMIN;
    }
}

package com.swiftpay.config;

import com.swiftpay.appUtils.ExchangeUtils;
import com.swiftpay.dto.ExchangeResponse;
import com.swiftpay.enums.AlertMessageTag;
import com.swiftpay.model.Office;
import com.swiftpay.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Office office = userService.getAuthenticated().getAgency().getOffice();

        ExchangeResponse exchangeResponse = getExchangeResponse(office.getCurrency());

        if(exchangeResponse != null) {
            ExchangeUtils.setExchangeResponse(exchangeResponse);
            ExchangeUtils.setInterestRate(office.getInterestRate());
            ExchangeUtils.setCountryOrigin(office.getName());
            response.sendRedirect("/");
        } else {
            logout(request, response);
            response.sendRedirect("/login?rate");
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }

    private ExchangeResponse getExchangeResponse(String targetCurrency) {

        String API_URL = "https://v6.exchangerate-api.com/v6/87790235b3bc72d839d084d8/latest";
        String url = String.format("%s/%s", API_URL, targetCurrency);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ExchangeResponse response = restTemplate.getForObject(url, ExchangeResponse.class);

            if (response != null && response.getResult().equals("success")) {
                return response;
            }
        } catch (ResourceAccessException ignored) {}

        return null;
    }
}


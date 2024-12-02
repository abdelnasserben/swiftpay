package com.swiftpay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public String showLoginPage(Model model) {

        model.addAttribute("pageTitle", "Connexion");
        return "login";
    }

}

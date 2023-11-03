package com.lutfi.adidatatest.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class dashboardController {
    @GetMapping("/dashboard")
    public String showRegistrationPage() {
        return "Dashboard";
    }
}

package com.lutfi.adidatatest.registration.password;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIResetPasswordController {
    @GetMapping("/reset-password")
    public String showRegistrationPage() {
        return "resetpassword";
    }
}


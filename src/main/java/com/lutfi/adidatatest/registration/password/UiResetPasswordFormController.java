package com.lutfi.adidatatest.registration.password;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiResetPasswordFormController {
    @GetMapping("/ui-reset-password")
    public String showRegistrationPage() {
        return "form-reset-password";
    }
}

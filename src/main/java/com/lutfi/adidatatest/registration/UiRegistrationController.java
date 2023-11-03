package com.lutfi.adidatatest.registration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiRegistrationController {

    @GetMapping("/registration")
    public String showRegistrationPage() {
        return "registration";
    }
}

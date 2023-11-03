package com.lutfi.adidatatest.User;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping(path ="/users")
public class UserController {
    private final  UserService userService;

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();

    }

}

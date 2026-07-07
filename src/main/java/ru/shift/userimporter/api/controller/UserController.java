package ru.shift.userimporter.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import ru.shift.userimporter.core.service.UserService;
import ru.shift.userimporter.core.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}

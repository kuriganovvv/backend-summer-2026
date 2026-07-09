package ru.shift.userimporter.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import ru.shift.userimporter.api.dto.ClientResponse;
import ru.shift.userimporter.core.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clients")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<ClientResponse> getAllUsers(@RequestParam(required = false)String phone, @RequestParam(required = false)String name, @RequestParam(required = false)String lastName, @RequestParam(required = false)String email,@RequestParam(required = false)Integer limit,@RequestParam(required = false)Integer offset){

        return userService.getUsersFiltered(phone,name,lastName,email,limit,offset);
    }

}

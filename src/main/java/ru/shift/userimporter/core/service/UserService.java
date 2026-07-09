package ru.shift.userimporter.core.service;

import ru.shift.userimporter.api.dto.ClientResponse;

import java.util.List;
public interface UserService{

    List<ClientResponse> getAllUsers();
    List<ClientResponse> getUsersFiltered(String phone, String name, String lastName, String email,Integer limit,Integer offset);
}

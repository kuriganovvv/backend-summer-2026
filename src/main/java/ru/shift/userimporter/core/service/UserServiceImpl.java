package ru.shift.userimporter.core.service;

import lombok.RequiredArgsConstructor;
import ru.shift.userimporter.api.dto.ClientResponse;
import ru.shift.userimporter.api.mapper.UserMapper;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<ClientResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toClientResponse)
                .collect(Collectors.toList());
    }
    @Override
    public List<ClientResponse> getUsersFiltered(String phone, String name, String lastName, String email,Integer limit,Integer offset){
        List<User> users = userRepository.findAll();
        Stream<User> stream = users.stream();
        if (phone != null && !phone.isBlank()) {
            stream = stream.filter(u -> u.getPhone().equals(phone));
        }
        if (name != null && !name.isBlank()) {
            stream = stream.filter(u -> u.getFirstName().equalsIgnoreCase(name));
        }
        if (lastName != null && !lastName.isBlank()) {
            stream = stream.filter(u -> u.getLastName().equalsIgnoreCase(lastName));
        }
        if (email != null && !email.isBlank()) {
            stream = stream.filter(u -> u.getEmail().equalsIgnoreCase(email));
        }

        return stream
                .map(userMapper::toClientResponse)
                .collect(Collectors.toList());
    }
}

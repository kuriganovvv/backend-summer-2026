package ru.shift.userimporter.core.service;

import lombok.RequiredArgsConstructor;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

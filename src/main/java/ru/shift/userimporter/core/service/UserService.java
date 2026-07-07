package ru.shift.userimporter.core.service;

import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
public interface UserService{

    List<User> getAllUsers();
}

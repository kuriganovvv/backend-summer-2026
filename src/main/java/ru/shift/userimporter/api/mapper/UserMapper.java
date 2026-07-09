package ru.shift.userimporter.api.mapper;

import org.springframework.stereotype.Component;
import ru.shift.userimporter.api.dto.ClientResponse;
import ru.shift.userimporter.core.model.User;

@Component
public class UserMapper{

    public ClientResponse toClientResponse(User user){
        if (user == null){
            return null;
        }

        return ClientResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .email(user.getEmail())
                .phone(user.getPhone() != null ? Long.parseLong(user.getPhone()) : null)
                .birthdate(user.getBirthDate() != null ? user.getBirthDate().toString() : null)
                .creationTime(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null)
                .updateTime(user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null)
                .build();
    }
}
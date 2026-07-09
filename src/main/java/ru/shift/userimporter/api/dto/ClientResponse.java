package ru.shift.userimporter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ClientResponse {
    private Long phone;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String birthdate;
    private String creationTime;
    private String updateTime;
}

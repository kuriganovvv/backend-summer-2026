package ru.shift.userimporter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProcessingError {
    private Integer lineNumber;
    private String errorCode;
    private String errorMessage;
}

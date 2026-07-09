package ru.shift.userimporter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DetailedFileStatistic {
    private Integer insertedLinesCount;
    private Integer updatedLinesCount;
    private List<ProcessingError> errors;

}

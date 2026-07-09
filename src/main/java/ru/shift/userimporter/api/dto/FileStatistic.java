package ru.shift.userimporter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FileStatistic {
    private Integer insertedLinesCount;         //Количество успешно добавленных строк
    private Integer updatedLinesCount;          //Количество успешно обновлённых строк
    private Integer errorProcessedLinesCount;   // Количество строк с ошибкой добавления/обновления


}

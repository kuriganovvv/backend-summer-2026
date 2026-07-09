package ru.shift.userimporter.api.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FileResponse {
    private String fileId; // Идентификатор файла
    private String status; // Статус файла
    private FileStatistic fileStatistic; //Информация о статистике по файлу

}

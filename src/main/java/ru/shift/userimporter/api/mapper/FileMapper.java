package ru.shift.userimporter.api.mapper;

import org.springframework.stereotype.Component;
import ru.shift.userimporter.api.dto.FileResponse;
import ru.shift.userimporter.api.dto.FileStatistic;
import ru.shift.userimporter.core.model.UploadedFile;

@Component
public class FileMapper{

    public FileResponse toFileResponse(UploadedFile file){
        if (file == null){
            return null;
        }

        return FileResponse.builder()
                .fileId(String.valueOf(file.getId()))
                .status(file.getStatus())
                .fileStatistic(FileStatistic.builder()
                        .insertedLinesCount(file.getValidRows())
                        .updatedLinesCount(0)
                        .errorProcessedLinesCount(file.getInvalidRows())
                        .build()
                )
                .build();
    }
}
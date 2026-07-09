package ru.shift.userimporter.core.service;

import org.springframework.web.multipart.MultipartFile;
import ru.shift.userimporter.api.dto.DetailedFileStatistic;
import ru.shift.userimporter.api.dto.FileResponse;
import ru.shift.userimporter.core.model.UploadedFile;

import java.util.List;


public interface UploadedFileService {
    UploadedFile saveFile(MultipartFile file);
    void processFile(Long fileId);
    List<FileResponse> getAllStatistics(String status);
    DetailedFileStatistic getDetailedStatistics(Long fileId);
    void checkFileExists(Long fileId);
}
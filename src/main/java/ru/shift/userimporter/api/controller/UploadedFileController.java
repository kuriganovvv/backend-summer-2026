package ru.shift.userimporter.api.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import ru.shift.userimporter.api.dto.DetailedFileStatistic;
import ru.shift.userimporter.api.dto.FileIdResponse;
import ru.shift.userimporter.api.dto.FileResponse;
import ru.shift.userimporter.core.model.UploadedFile;
import ru.shift.userimporter.core.service.UploadedFileService;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/files")
public class UploadedFileController {
    private final UploadedFileService uploadedFileService;

    @PostMapping
    public ResponseEntity<FileIdResponse>uploadFile(@RequestParam("file")MultipartFile file){
        UploadedFile savedFile = uploadedFileService.saveFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FileIdResponse(savedFile.getId().toString()));
    }
    @PostMapping("/{fileId}/processing")
    public ResponseEntity<Void> processFile(@PathVariable String fileId){
        uploadedFileService.checkFileExists(Long.parseLong(fileId));
        uploadedFileService.processFile(Long.parseLong(fileId));
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/statistics")
    public List<FileResponse> filesStatistics(@RequestParam(required = false) String status){
        uploadedFileService.getAllStatistics(status);
        return uploadedFileService.getAllStatistics(status);
    }
    @GetMapping("/{fileId}/statistics")
    public DetailedFileStatistic fileDetailedStatistic(@PathVariable String fileId){
        return uploadedFileService.getDetailedStatistics(Long.parseLong(fileId));
    }

}

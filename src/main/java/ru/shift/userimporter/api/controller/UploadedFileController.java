package ru.shift.userimporter.api.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import ru.shift.userimporter.api.dto.FileIdResponse;
import ru.shift.userimporter.core.model.UploadedFile;
import ru.shift.userimporter.core.service.UploadedFileService;


import java.util.List;

@RestController
@RequestMapping("api/v1/files")
public class UploadedFileController {
    private final UploadedFileService uploadedFileService;
    public UploadedFileController(UploadedFileService uploadedFileService){
        this.uploadedFileService = uploadedFileService;
    }
    @PostMapping
    public ResponseEntity<FileIdResponse>uploadFile(@RequestParam("file")MultipartFile file){
        UploadedFile savedFile = uploadedFileService.saveFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FileIdResponse(savedFile.getId().toString()));
    }



}

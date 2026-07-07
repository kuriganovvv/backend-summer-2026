package ru.shift.userimporter.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.shift.userimporter.core.model.FileProcessingErrors;
import ru.shift.userimporter.core.model.UploadedFile;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.repository.FileProcessingErrorsRepository;
import ru.shift.userimporter.core.repository.UploadedFileRepository;
import ru.shift.userimporter.core.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class UploadedFileServiceImpl implements UploadedFileService {
    private final UploadedFileRepository uploadedFileRepository;
    private final UserRepository userRepository;
    private final FileProcessingErrorsRepository fileProcessingErrorsRepository;

    public UploadedFile saveFile(MultipartFile file){
        String originalName = file.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID() + "_" + originalName;
        Path uploadPath = Paths.get("uploads");
        try{
            Files.createDirectories(uploadPath);
        }catch(IOException e){
            throw new RuntimeException("Не удалось создать папку uploads",e);
        }
        Path filePath = uploadPath.resolve(uniqueFilename);
        try(InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new RuntimeException("Не удалось сохранить файл", e);
        }
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setOriginalFilename(originalName);
        uploadedFile.setStoragePath(filePath.toString());
        uploadedFile.setStatus("NEW");

        return uploadedFileRepository.save(uploadedFile);

    }

    public void processFile(Long fileId) {
        UploadedFile file = uploadedFileRepository.findById(fileId).orElseThrow(() -> new RuntimeException("Файл не найден"));
        file.setStatus("IN_PROGRESS");
        uploadedFileRepository.save(file);
        Path filePath = Paths.get(file.getStoragePath());
        AtomicInteger validRows = new AtomicInteger(0);
        AtomicInteger invalidRows = new AtomicInteger(0);
        AtomicInteger rowNumber = new AtomicInteger(0);
        try (Stream<String> lines = Files.lines(filePath)) {
            lines.forEach(line -> {
                rowNumber.incrementAndGet();
                try {
                    String[] fields = line.split(",");
                    if (fields.length != 6) throw new RuntimeException("Неверный формат");
                    User user = new User(null, fields[0], fields[1], fields[2], fields[3], fields[4], LocalDate.parse(fields[5]), null, null);

                    if (!(user.getFirstName().matches("^[А-ЯЁ][а-яёА-ЯЁ'\\- ]{2,}$") && user.getFirstName().length() >= 3 && user.getFirstName().length() <= 50)) {
                        throw new IllegalArgumentException("Некорректное имя");
                    }
                    if (!(user.getLastName().matches("^[А-ЯЁ][а-яёА-ЯЁ'\\- ]{2,}$") && user.getLastName().length() >= 3 && user.getLastName().length() <= 50)) {
                        throw new IllegalArgumentException("Некорректная фамиилия");
                    }
                    if (user.getMiddleName() != null && !user.getMiddleName().isBlank()) {
                        if (!(user.getMiddleName().matches("^[А-ЯЁ][а-яёА-ЯЁ'\\- ]{2,}$") || user.getMiddleName().length() < 3 || user.getMiddleName().length() > 50)) {
                            throw new IllegalArgumentException("Некорректное отчество");
                        }
                    }
                    if (!(user.getEmail().matches("^[A-Za-z0-9._%-]+@(shift\\.com|shift\\.ru)$") && user.getEmail().length() <= 100)) {
                        throw new IllegalArgumentException("Некорректная почта");
                    }
                    if (!(user.getPhone().matches("^7[0-9]{10}$"))) {
                        throw new IllegalArgumentException("Некорректный телефон");
                    }
                    if (LocalDate.now().getYear() - user.getBirthDate().getYear() < 18) {
                        throw new IllegalArgumentException("Некорректный возраст");
                    }
                    userRepository.save(user);
                    validRows.incrementAndGet();
                } catch (Exception e) {
                    FileProcessingErrors error = new FileProcessingErrors();
                    error.setFileId(Math.toIntExact(fileId));
                    error.setRowNumber(rowNumber.get());
                    error.setErrorMessage(e.getMessage());
                    error.setRawData(line);
                    fileProcessingErrorsRepository.save(error);
                    invalidRows.incrementAndGet();
                }
            });

        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл");
        }
        file.setTotalRows(rowNumber.get());
        file.setValidRows(validRows.get());
        file.setInvalidRows(invalidRows.get());
        file.setStatus("DONE");
        uploadedFileRepository.save(file);
    }
}

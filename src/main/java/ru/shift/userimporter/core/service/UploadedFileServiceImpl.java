package ru.shift.userimporter.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.shift.userimporter.api.dto.DetailedFileStatistic;
import ru.shift.userimporter.api.dto.FileResponse;
import ru.shift.userimporter.api.dto.FileStatistic;
import ru.shift.userimporter.api.dto.ProcessingError;
import ru.shift.userimporter.api.mapper.FileMapper;
import ru.shift.userimporter.core.exception.ValidationException;
import ru.shift.userimporter.core.model.FileProcessingError;
import ru.shift.userimporter.core.model.UploadedFile;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.repository.FileProcessingErrorsRepository;
import ru.shift.userimporter.core.repository.UploadedFileRepository;
import ru.shift.userimporter.core.repository.UserRepository;
import ru.shift.userimporter.core.exception.ResourseNotFountException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class UploadedFileServiceImpl implements UploadedFileService {
    private final UploadedFileRepository uploadedFileRepository;
    private final UserRepository userRepository;
    private final FileProcessingErrorsRepository fileProcessingErrorsRepository;
    private final FileMapper fileMapper;

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
    @Transactional
    @Async
    public void processFile(Long fileId) {
        UploadedFile file = uploadedFileRepository.findById(fileId).orElseThrow(() -> new ResourseNotFountException("Файл не найден"));
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
                    if (fields.length != 6) throw new ValidationException("Неверный формат");
                    User user = User.builder()
                        .firstName(fields[0])
                        .lastName(fields[1])
                        .middleName(fields[2])
                        .email(fields[3])
                        .phone(fields[4])
                        .birthDate(LocalDate.parse(fields[5]))
                        .build();

                    if (!(user.getFirstName().matches("^[А-ЯЁ][а-яёА-ЯЁ'\\- ]{2,}$") && user.getFirstName().length() >= 3 && user.getFirstName().length() <= 50)) {
                        throw new ValidationException("Некорректное имя");
                    }
                    if (!(user.getLastName().matches("^[А-ЯЁ][а-яёА-ЯЁ'\\- ]{2,}$") && user.getLastName().length() >= 3 && user.getLastName().length() <= 50)) {
                        throw new ValidationException("Некорректная фамиилия");
                    }
                    if (user.getMiddleName() != null && !user.getMiddleName().isBlank()) {
                        if (!(user.getMiddleName().matches("^[А-ЯЁ][а-яёА-ЯЁ'\\- ]{2,}$") || user.getMiddleName().length() < 3 || user.getMiddleName().length() > 50)) {
                            throw new ValidationException("Некорректное отчество");
                        }
                    }
                    if (!(user.getEmail().matches("^[A-Za-z0-9._%-]+@(shift\\.com|shift\\.ru)$") && user.getEmail().length() <= 100)) {
                        throw new ValidationException("Некорректная почта");
                    }
                    if (!(user.getPhone().matches("^7[0-9]{10}$"))) {
                        throw new ValidationException("Некорректный телефон");
                    }
                    if (userRepository.existsByPhone(user.getPhone())) {
                        throw new ValidationException("Телефон уже существует: " + user.getPhone());
                    }
                    if (userRepository.existsByEmail(user.getEmail())) {
                        throw new ValidationException("Email уже существует: " + user.getEmail());
                    }
                    if (LocalDate.now().getYear() - user.getBirthDate().getYear() < 18) {
                        throw new ValidationException("Некорректный возраст");
                    }

                    userRepository.save(user);
                    validRows.incrementAndGet();
                } catch (Exception e) {
                    FileProcessingError error = FileProcessingError.builder()
                            .fileId(Math.toIntExact(fileId))
                            .rowNumber(rowNumber.get())
                            .errorMessage(e.getMessage())
                            .rawData(line)
                            .errorCode("VALIDATION_ERROR")
                            .build();
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
        file.setProcessedRows(rowNumber.get());
        file.setStatus("DONE");
        uploadedFileRepository.save(file);
    }

    @Override
    public List<FileResponse> getAllStatistics(String status) {
        List<UploadedFile> files;
        if(status!=null && !status.isBlank()){
            files=uploadedFileRepository.findByStatus(status);
        }else{
            files = uploadedFileRepository.findAll();
        }
        return files.stream()
                .map(fileMapper::toFileResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DetailedFileStatistic getDetailedStatistics(Long fileId) {
        UploadedFile file = uploadedFileRepository.findById(fileId).orElseThrow(() -> new ResourseNotFountException("Файл не найден"));

        List<ProcessingError> errors = fileProcessingErrorsRepository.findByFileId(Math.toIntExact(fileId))
                .stream()
                .map(e -> ProcessingError.builder()
                        .lineNumber(e.getRowNumber())
                        .errorCode(e.getErrorCode())
                        .errorMessage(e.getErrorMessage())
                        .build()
                )
                .collect(Collectors.toList());

        return DetailedFileStatistic.builder()
                .insertedLinesCount(file.getValidRows())
                .updatedLinesCount(file.getProcessedRows())
                .errors(errors)
                .build();
    }
    public void checkFileExists(Long fileId){
        uploadedFileRepository.findById(fileId).orElseThrow(()->new ResourseNotFountException("Файл не найден"));
    }
}

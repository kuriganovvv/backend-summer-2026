package ru.shift.userimporter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shift.userimporter.core.model.FileProcessingError;

import java.util.List;

public interface FileProcessingErrorsRepository extends JpaRepository<FileProcessingError,Long> {
    List<FileProcessingError> findByFileId(Integer fileId);
}

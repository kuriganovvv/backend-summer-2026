package ru.shift.userimporter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shift.userimporter.core.model.FileProcessingErrors;

public interface FileProcessingErrorsRepository extends JpaRepository<FileProcessingErrors,Long> {
}

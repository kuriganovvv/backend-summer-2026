package ru.shift.userimporter.core.repository;

import ru.shift.userimporter.core.model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile,Long>{

    List<UploadedFile> findByStatus(String status);

    Optional<Object> findByHash(String hash);
}

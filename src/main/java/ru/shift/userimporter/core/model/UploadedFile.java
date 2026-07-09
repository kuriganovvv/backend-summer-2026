package ru.shift.userimporter.core.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "uploaded_files")
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "total_rows")
    private Integer totalRows;
    @Column(name = "processed_rows")
    private Integer processedRows;
    @Column(name = "valid_rows")
    private Integer validRows;
    @Column(name = "invalid_rows")
    private Integer invalidRows;
    @Column(name = "original_filename")
    private String originalFilename;
    @Column(name = "storage_path")
    private String storagePath;
    @Column(name = "status")
    private String status;
    @Column(name = "hash")
    private String hash;
}
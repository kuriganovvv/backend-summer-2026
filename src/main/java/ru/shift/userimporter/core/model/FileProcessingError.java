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
@Table(name = "file_processing_errors")
public class FileProcessingError{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "file_id")
    private Integer fileId;
    @Column(name = "row_number")
    private Integer rowNumber;
    @Column(name = "error_message")
    private String errorMessage;
    @Column(name = "raw_data")
    private String rawData;
    @Column(name = "error_code")
    private String errorCode;
}
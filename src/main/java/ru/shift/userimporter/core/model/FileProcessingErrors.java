package ru.shift.userimporter.core.model;

import jakarta.persistence.*;

@Entity
@Table(name = "file_processing_errors")
public class FileProcessingErrors {

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

    public FileProcessingErrors() {
    }

    public FileProcessingErrors(Long id, Integer fileId, Integer rowNumber,
                                String errorMessage, String rawData, String errorCode) {
        this.id = id;
        this.fileId = fileId;
        this.rowNumber = rowNumber;
        this.errorMessage = errorMessage;
        this.rawData = rawData;
        this.errorCode = errorCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
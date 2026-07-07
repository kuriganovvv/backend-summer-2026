package ru.shift.userimporter.api.dto;

public class FileIdResponse{
    private final String fileId;
    public FileIdResponse(String fileId){
        this.fileId=fileId;
    }

    public String getFileId() {
        return fileId;
    }
}

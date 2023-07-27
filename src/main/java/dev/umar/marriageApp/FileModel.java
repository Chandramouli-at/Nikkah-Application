//package dev.umar.marriageApp;
//
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//@Document(collection = "files")
//public class FileModel {
//    @Id
//    private String id;
//    private String fileName;
//    private String fileType;
//    private byte[] fileData;
//
//    // Constructors, getters, and setters
//
//    public FileModel() {
//    }
//
//    public FileModel(String fileName, String fileType, byte[] fileData) {
//        this.fileName = fileName;
//        this.fileType = fileType;
//        this.fileData = fileData;
//    }
//
//    // Getters and setters
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public String getFileType() {
//        return fileType;
//    }
//
//    public void setFileType(String fileType) {
//        this.fileType = fileType;
//    }
//
//    public byte[] getFileData() {
//        return fileData;
//    }
//
//    public void setFileData(byte[] fileData) {
//        this.fileData = fileData;
//    }
//}

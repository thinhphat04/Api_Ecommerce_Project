package com.phat.api_flutter.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService() {
        this.fileStorageLocation = Paths.get("public/uploads").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = file.getOriginalFilename().replace(" ", "-").replace(".png", "").replace(".jpg", "").replace(".jpeg", "");
        String fileExtension = getFileExtension(file.getContentType());

        if (fileExtension == null) {
            throw new RuntimeException("Invalid file type");
        }

        fileName = fileName + "-" + System.currentTimeMillis() + "." + fileExtension;

        try {
            // Copy file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public void deleteFiles(String[] fileNames, String continueOnErrorName) {
        for (String fileName : fileNames) {
            try {
                Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
                Files.deleteIfExists(filePath);
            } catch (IOException ex) {
                if (ex.getMessage().contains(continueOnErrorName)) {
                    System.err.println("Continuing with the next file: " + ex.getMessage());
                } else {
                    System.err.println("Error deleting file: " + ex.getMessage());
                    throw new RuntimeException("Could not delete file " + fileName + ". Please try again!", ex);
                }
            }
        }
    }

    private String getFileExtension(String mimeType) {
        switch (mimeType) {
            case "image/png":
                return "png";
            case "image/jpeg":
                return "jpeg";
            case "image/jpg":
                return "jpg";
            default:
                return null;
        }
    }
}

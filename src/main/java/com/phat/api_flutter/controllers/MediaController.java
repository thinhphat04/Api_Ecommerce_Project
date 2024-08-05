package com.phat.api_flutter.controllers;

import com.phat.api_flutter.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class MediaController {
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileStorageService.storeFile(file);
            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Could not upload file: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFiles(@RequestParam("fileNames") String[] fileNames) {
        try {
            fileStorageService.deleteFiles(fileNames, "NoSuchFileException");
            return ResponseEntity.ok("Files deleted successfully");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Could not delete files: " + ex.getMessage());
        }
    }
}

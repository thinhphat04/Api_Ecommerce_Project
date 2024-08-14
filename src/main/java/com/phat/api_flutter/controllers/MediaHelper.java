package com.phat.api_flutter.controllers;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class MediaHelper {

    //Mỗi máy lưu 1 dường dẫn khác nhau
    //Đây là đường dẫn cua Dung
    private static final String UPLOAD_DIR = "C:\\FPT Aptech\\4.Eproject\\NewEproject4\\Ecommerce_Ecomly_Project4-main\\server\\public\\uploads\\";


    public String uploadFile(MultipartFile file) throws IOException {
        final String baseUrl =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();


        // Kiểm tra định dạng file
        String mimeType = file.getContentType();
        if (!mimeType.equals("image/png") && !mimeType.equals("image/jpeg") && !mimeType.equals("image/jpg")) {
            throw new IOException("Invalid image type " + mimeType + " is not allowed");
        }

        // Tạo tên file
        String originalFilename = file.getOriginalFilename();
        String fileName = originalFilename != null ? originalFilename.replaceAll(" ", "-").replaceAll("\\.(png|jpg|jpeg)$", "") : "";
        String extension = mimeType.split("/")[1];
        String newFileName = fileName + "-" + System.currentTimeMillis() + "." + extension;

        // Lưu file
        Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR + newFileName), StandardCopyOption.REPLACE_EXISTING);

        return baseUrl + "/public/uploads/" + newFileName;
    }

    public void deleteImages(String[] imageUrls) throws IOException {
        for (String imageUrl : imageUrls) {
            File file = new File(imageUrl);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}

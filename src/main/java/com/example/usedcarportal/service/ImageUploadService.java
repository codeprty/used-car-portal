package com.example.usedcarportal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class ImageUploadService {
    private static final String UPLOAD_DIR = "uploads/";

    public String saveImage(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            // Ensure upload directory exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save file
            String filePath = UPLOAD_DIR + file.getOriginalFilename();
            file.transferTo(Paths.get(filePath));

            return "/" + filePath; // Return relative path for retrieval
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.example.usedcarportal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class ImageUploadService {

    private static final String UPLOAD_DIR = "uploads/"; // Define upload directory

    // Save image to the server
    public String saveImage(MultipartFile file) {
        if (file.isEmpty()) {
            return null; // Return null if file is empty
        }

        try {
            // Ensure the upload directory exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Create the directory if it doesn't exist
            }

            // Save the file to the upload directory
            String filePath = UPLOAD_DIR + file.getOriginalFilename(); // Get the file path
            file.transferTo(Paths.get(filePath)); // Save file

            return "/" + filePath; // Return the relative file path for retrieval
        } catch (IOException e) {
            e.printStackTrace(); // Print exception stack trace for debugging
            return null; // Handle exception and return null if file saving fails
        }
    }
}

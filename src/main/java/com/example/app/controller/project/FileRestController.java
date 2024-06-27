package com.example.app.controller.project;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.service.S3.S3Uploader;
import com.example.app.domain.service.project.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class FileRestController {

    @Autowired
    private S3Uploader s3Uploader;

    @Autowired
    private ProjectServiceImpl projectServiceImpl;

    // 파일 삭제
    @PostMapping("/delete")
    public ResponseEntity<String> fileDelete(@RequestParam("uploadedUrl") String uploadedUrl) {
        try {
            s3Uploader.deleteFile(uploadedUrl);
            return ResponseEntity.ok("File deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete file: " + e.getMessage());
        }
    }

}

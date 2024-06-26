package com.example.app.controller.project;

import com.example.app.domain.service.S3.S3Uploader;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class FileRestController {

    @Autowired
    private S3Uploader s3Uploader;

    // 다중 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> filesUpload(@RequestPart("multipartFiles") List<MultipartFile> multipartFiles) {
        List<String> uploadedUrls = s3Uploader.saveFiles(multipartFiles);
        return ResponseEntity.ok("Files uploaded successfully. URL: " + uploadedUrls);
    }

    // 파일 삭제
    @PostMapping("delete")
    public ResponseEntity<String> fileDelete(@RequestParam("uploadedUrl") String uploadedUrl) {
        try {
            s3Uploader.deleteFile(uploadedUrl);
            return ResponseEntity.ok("File deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete file: " + e.getMessage());
        }
    }

}

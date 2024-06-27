package com.example.app.domain.service.project;

import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImageResizer {

    public List<MultipartFile> resizeImages(List<MultipartFile> multipartFileList) throws IOException {
        List<MultipartFile> resizedMultipartFiles = new ArrayList<>();

        for (MultipartFile file : multipartFileList) {
            // MultipartFile -> BufferedImage
            BufferedImage bi = ImageIO.read(file.getInputStream());
            // 이미지 사이즈 변경
            BufferedImage resizedImage = resizeImage(bi, 636, 477);
            // BufferedImage -> MultipartFile
            MultipartFile resizedMultipartFile = convertBufferedImageToMultipartFile(resizedImage, file.getOriginalFilename(), getFileExtension(file.getOriginalFilename()));
            resizedMultipartFiles.add(resizedMultipartFile);
        }
        return resizedMultipartFiles;
    }

    // 이미지 라사이징 해주는 메서드
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        // resize 에 들어가는  속성을 변경해서 여러 모드로 변경 가능
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }

    // BufferedImage -> MultipartFile 변환 메서드
    private MultipartFile convertBufferedImageToMultipartFile(BufferedImage image, String filename, String formatName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, formatName, baos);
        byte[] imageBytes = baos.toByteArray();
        baos.close();

        return new CustomMultipartFile(imageBytes, filename, "image/" + formatName);
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String filename) {
        int lastIndexOf = filename.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // 확장자가 없는 경우
        }
        return filename.substring(lastIndexOf + 1);
    }

}

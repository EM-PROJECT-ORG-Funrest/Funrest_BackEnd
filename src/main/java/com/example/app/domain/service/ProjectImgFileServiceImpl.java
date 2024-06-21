package com.example.app.domain.service;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ProjectImgFileServiceImpl {
    public void uploadFile(MultipartFile file, String savePath) throws IOException {
        // MultipartFile -> BufferedImage
        BufferedImage bi = ImageIO.read(file.getInputStream());
        // 이미지 사이즈 변경
        bi = resizeImage(bi, 636, 477);
        // 저장 경로 위치에 이미지 저장
        ImageIO.write(bi, "jpg", new File(savePath));
        ImageIO.write(bi, "png", new File(savePath));
    }
    // 이미지 라사이징 해주는 메서드
    private BufferedImage resizeImage(BufferedImage originalImage,int targetWidth,int targetHeight) {
        // resize 에 들어가는  속성을 변경해서 여러 모드로 변경 가능
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }


}

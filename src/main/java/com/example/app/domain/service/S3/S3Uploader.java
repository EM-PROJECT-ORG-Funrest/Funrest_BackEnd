package com.example.app.domain.service.S3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tags.shaded.org.apache.xalan.xsltc.runtime.ErrorMessages;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

// AmazonS3 클라이언트를 사용해 S3와의 상호작용을 처리하는 클래스
@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {

    // AWS S3 클라이언트 (파일을 S3에 업로드하거나 삭제하는 데 사용)
    private final AmazonS3 amazonS3;
    // 업로드된 파일의 이름을 저장하는 Set (중복 업로드를 방지하기 위해 사용)
    private Set<String> uploadedFileNames = new HashSet<>();
    // 업로드된 파일의 크기를 저장하는 Set (중복 업로드를 방지하기 위해 사용)
    private Set<Long> uploadedFileSizes = new HashSet<>();
    // S3 버킷 이름 저장
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    // 업로드 가능한 최대 파일 크기 저장
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxSizeString;

    // 다중 파일 업로드 (여러 파일을 S3에 업로드 후 파일의 URL 저장 리스트 반환)
    public List<String> saveFiles(List<MultipartFile> multipartFiles) {
        // 1. 업로드된 파일의 URL 저장 리스트
        List<String> filePaths = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            // 2. 파일 중복 검사
            if (isDuplicate(multipartFile)) {
                throw new RuntimeException("file duplicated");
            }
            // 3. 단일 파일로 업로드 후 URL 반환 후 저장
            String uploadedUrl = saveFile(multipartFile);
            // 4. 기본 URL 경로와 파일 경로 분리
            String baseUrl = "https://funrestbucket.s3.ap-northeast-2.amazonaws.com/";
            String filePath = uploadedUrl.substring(baseUrl.length());

            // 5. 파일 경로만 리스트에 추가
            filePaths.add(filePath);
        }
        // 5. 중복 체크를 위해 저장된 파일 정보 초기화
        clear();
        // 6. 업로드된 파일의 URL 저장 리스트 반환
        return filePaths;
    }

    // 파일 삭제 (주어진 URL의 파일을 S3에서 삭제)
    public void deleteFile(String fileUrl) {
        // 1. URL을 '/'로 분할
        String[] urlParts = fileUrl.split("/");
        // 2. 버킷 이름 추출
        String fileBucket = urlParts[2].split("\\.")[0];
        // 3. 버킷 이름이 일치하지 않으면 예외 발생
        if (!fileBucket.equals(bucket)) {
            throw new RuntimeException("file delete failed");
        }
        // 4. 객체 키 생성
        String objectKey = String.join("/", Arrays.copyOfRange(urlParts, 3, urlParts.length));
        // 5. 객체가 존재하지 않으면 예외 발생
        if (!amazonS3.doesObjectExist(bucket, objectKey)) {
            throw new RuntimeException("file delete failed");
        }
        // 6. 객체 삭제
        try {
            amazonS3.deleteObject(bucket, objectKey);
        } catch (AmazonS3Exception e) {
            log.error("File delete fail : " + e.getMessage());
            throw new RuntimeException("file delete success");
        } catch (SdkClientException e) {
            log.error("AWS SDK client error : " + e.getMessage());
            throw new RuntimeException("file delete success");
        }
        // 7. 삭제 완료 로그 출력
        log.info("File delete complete: " + objectKey);
    }

    // 단일 파일 저장 (단일 파일을 S3에 업로드하고 업로드된 파일의 URL을 반환)
    public String saveFile(MultipartFile file) {
        // 1. 랜덤 파일 이름 생성
        String randomFilename = generateRandomFilename(file);
        log.info("File upload started: " + randomFilename);
        // 2. 메타데이터 생성 (데이터에 대한 정보(데이터))
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        try {
            // 3. S3에 파일 업로드 (amazonS3 클래스의 내장 메소드 사용)
            amazonS3.putObject(bucket, randomFilename, file.getInputStream(), metadata);
        } catch (AmazonS3Exception e) {
            log.error("Amazon S3 error while uploading file: " + e.getMessage());
            throw new RuntimeException("file upload failed");
        } catch (SdkClientException e) {
            log.error("AWS SDK client error while uploading file: " + e.getMessage());
            throw new RuntimeException("file upload failed");
        } catch (IOException e) {
            log.error("IO error while uploading file: " + e.getMessage());
            throw new RuntimeException("file upload failed");
        }
        // 업로드 완료 로그 출력
        log.info("File upload completed: " + randomFilename);
        // 업로드된 파일의 URL 반환
        return amazonS3.getUrl(bucket, randomFilename).toString();
    }

    // 중복되는 파일 여부 확인
    private boolean isDuplicate(MultipartFile multipartFile) {
        // 1. 파일 이름
        String fileName = multipartFile.getOriginalFilename();
        // 2. 파일 크기
        Long fileSize = multipartFile.getSize();
        // 3. 파일 이름과 크기 모두 중복 시 true 반환
        if (uploadedFileNames.contains(fileName) && uploadedFileSizes.contains(fileSize)) {
            return true;
        }
        // 파일 이름, 크기 저장
        uploadedFileNames.add(fileName);
        uploadedFileSizes.add(fileSize);
        // 중복 아니면 false 반환
        return false;
    }

    // 중복 체크를 위해 저장된 파일 정보 초기화 메서드 (Set 을 초기화하여 각 파일 업로드 요청이 독립적으로 처리)
    private void clear() {
        // 파일 이름, 파일 크기 초기화
        uploadedFileNames.clear();
        uploadedFileSizes.clear();
    }

    // 랜덤파일명 생성 (파일명 중복 방지)
    private String generateRandomFilename(MultipartFile multipartFile) {
        // 1. 원본 파일 이름
        String originalFilename = multipartFile.getOriginalFilename();
        // 2. 파일 확장자 검증 메소드 호출 및 반환값
        String fileExtension = validateFileExtension(originalFilename);
        // 3. 랜덤 파일 이름
        String randomFilename = UUID.randomUUID() + "." + fileExtension;
        // 4. 랜덤 파일명 반환
        return randomFilename;
    }

    // 파일 확장자 체크
    private String validateFileExtension(String originalFilename) {
        // 1. 확장자 추출
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        // 2. 허용된 확장자 목록 지정
        List<String> allowedExtensions = Arrays.asList("jpg", "png", "gif", "jpeg");
        // 3. 파일 확장자 체크
        if (!allowedExtensions.contains(fileExtension)) {
            // 4. 허용되지 않은 확장자면 예외 발생
            throw new RuntimeException("not file extension");
        }
        // 5. 파일 확장자 반환
        return fileExtension;
    }
}

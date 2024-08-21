package com.snowflake_guide.tourmate.domain.review.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * AWS S3 관련 작업을 처리하는 서비스 클래스.
 */
@Slf4j
@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    /**
     * 파일을 S3에 업로드합니다.
     *
     * @param dirName       S3 버킷 내 디렉토리 이름
     * @param fileName      원본 파일 이름
     * @param multipartFile 업로드할 MultipartFile
     * @return 업로드된 파일의 S3 URL
     * @throws IOException 파일 변환 중 에러 발생 시
     */
    public String upload(String dirName, String fileName, MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile을 File로 전환하는 데 실패했습니다."));
        return upload(dirName, fileName, uploadFile);
    }

    /**
     * 파일을 S3에 업로드하고 URL을 반환합니다.
     *
     * @param dirName    S3 버킷 내 디렉토리 이름
     * @param fileName   원본 파일 이름
     * @param uploadFile 업로드할 파일
     * @return 업로드된 파일의 S3 URL
     */
    private String upload(String dirName, String fileName, File uploadFile) {
        String uniqueFileName = generateUniqueFilename(fileName);
        String newFileName = dirName + "/" + uniqueFileName;
        String uploadImageUrl = putS3(uploadFile, newFileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    /**
     * 파일을 S3에 업로드합니다.
     *
     * @param uploadFile 업로드할 파일
     * @param fileName   S3 버킷 내 파일 이름
     * @return 업로드된 파일의 S3 URL
     */
    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead) // PublicRead 권한으로 업로드
        );
        log.info("Bucket: {}", bucket);
        log.info("FileName: {}", fileName);
        return generateS3Url(fileName);
    }

    private String generateS3Url(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, "ap-northeast-2", fileName);
    }

    /**
     * S3에서 파일을 삭제합니다.
     *
     * @param fileUrl 삭제할 파일의 URL
     */
    public void delete(String fileUrl) {
        String fileKey = extractFileKey(fileUrl);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileKey));
    }

    /**
     * 업로드 과정에서 생성된 임시 파일을 삭제합니다.
     *
     * @param targetFile 삭제할 파일
     */
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일 삭제에 실패했습니다.");
        }
    }

    /**
     * MultipartFile을 File로 변환합니다.
     *
     * @param file 변환할 MultipartFile
     * @return 변환된 파일을 포함하는 Optional 객체
     * @throws IOException 변환 중 에러 발생 시
     */
    private Optional<File> convert(MultipartFile file) throws IOException {
        log.info(file.getOriginalFilename());
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    /**
     * UUID를 사용하여 고유한 파일 이름을 생성합니다.
     *
     * @param originalFilename 원본 파일 이름
     * @return 고유한 파일 이름
     */
    private String generateUniqueFilename(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
            originalFilename = originalFilename.substring(0, dotIndex);
        }
        return originalFilename + "_" + uuid + extension;
    }

    /**
     * 파일 URL에서 파일 키를 추출합니다.
     *
     * @param fileUrl 파일 URL
     * @return 파일 키
     */
    private String extractFileKey(String fileUrl) {
        String prefix = String.format("https://%s.s3.%s.amazonaws.com/", bucket, "ap-northeast-2");
        return fileUrl.substring(prefix.length());
    }
}
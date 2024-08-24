package com.snowflake_guide.tourmate.domain.review.api;

import com.snowflake_guide.tourmate.domain.review.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * S3 파일 작업을 처리하는 REST 컨트롤러.
 */
@Tag(name= "S3 API", description = "AWS S3와의 연동을 테스트하는 API")
@RestController
@Slf4j
@RequestMapping("/api")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * 파일을 S3에 업로드하는 엔드포인트.
     *
     * @param testFile 업로드할 MultipartFile
     * @return 업로드된 파일의 URL을 포함하는 ResponseEntity
     * @throws IOException 파일 업로드 중 에러 발생 시
     */
    @Operation(summary = "S3에 파일 업로드", description = "지정된 S3 버킷에 파일을 업로드하고 파일 URL을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일이 성공적으로 업로드됨",
                    content = {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping(path = "/s3/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart(value = "testFile") @RequestBody(description = "업로드할 파일") MultipartFile testFile
    ) throws IOException {
        String testFileName = testFile.getOriginalFilename();
        String testFileUrl = s3Service.upload("mytest", testFileName, testFile);
        log.info("testFile Url is : " + testFileUrl);
        return new ResponseEntity<>("testFile URL: " + testFileUrl, HttpStatus.OK);
    }

    /**
     * S3에서 파일을 삭제하는 엔드포인트.
     *
     * @param imageUrl 삭제할 파일의 URL
     * @return 삭제 상태를 포함하는 ResponseEntity
     */
    @Operation(summary = "S3에서 파일 삭제", description = "제공된 파일 URL을 기반으로 지정된 S3 버킷에서 파일을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일이 성공적으로 삭제됨", content = @Content),
            @ApiResponse(responseCode = "500", description = "파일 삭제 실패", content = @Content)
    })
    @DeleteMapping(path = "/s3/test")
    public ResponseEntity<String> deleteFile1(
            @RequestParam(value = "fileUrl") @RequestBody(description = "삭제할 파일의 URL") String imageUrl
    ) {
        try {
            s3Service.delete(imageUrl); // S3 삭제
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file");
        }
    }
}
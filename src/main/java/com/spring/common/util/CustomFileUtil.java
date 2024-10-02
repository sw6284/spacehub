package com.spring.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomFileUtil {

    // 변경된 파일 경로 반영
    //@Value("${upload.space.path:/SpaceHub/attachment/spaceImg}")
    //private String uploadPath;
	
	@Value("${com.spring.upload.path}")
	private String uploadPath;
	
    public void init() {
        File tempFolder = new File(uploadPath);
        if (!tempFolder.exists()) { // 폴더가 없으면 생성
            tempFolder.mkdir();
        }
        uploadPath = tempFolder.getAbsolutePath();
        
        log.info("-------------------------------------");
        log.info(uploadPath);
    }

    /* 썸네일 이미지 및 파일 업로드  */
    public String saveFile(MultipartFile file, String imgDir) throws RuntimeException {
        if (file == null || file.isEmpty()) { 
            return null; 
        }
        String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path savePath = Paths.get(uploadPath, imgDir , savedName);
        try {
            Files.copy(file.getInputStream(), savePath);
            String contentType = file.getContentType();
            
            // 이미지 여부 확인
            if (contentType != null && contentType.startsWith("image")) { 
                Path thumbnailPath = Paths.get(uploadPath, imgDir, "s_" + savedName);
                Thumbnails.of(savePath.toFile()).size(200, 133).toFile(thumbnailPath.toFile());
            }
        } catch (IOException e) {
            log.error("File save failed", e);
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
        return savedName;
    }
    
    /* 업로드 파일 보여주기 위한 메서드 정의 - getFile() 추가 */
    public ResponseEntity<Resource> getFile(String fileName, String imgDir) {
        Path filePath = Paths.get(uploadPath, imgDir, fileName);
        Resource resource = new FileSystemResource(filePath.toFile());
        if (!resource.exists()) {
            filePath = Paths.get(uploadPath, imgDir, "default.jpg");
            resource = new FileSystemResource(filePath.toFile());
        }
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(filePath));
        } catch (Exception e) {
            log.error("Failed to determine content type", e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }
    
    /* 썸네일 파일 및 파일 삭제
	public void deleteFile(String fileName, String imgDir){
	 	if(fileName == null){
	 		return;
	 	}
	 	//썸네일이 있는지 확인하고 삭제
	 	String thumbnailFileName = "s_" + fileName;
		Path thumbnailPath = Paths.get(uploadPath, imgDir, thumbnailFileName);
		Path filePath = Paths.get(uploadPath, fileName);

		try {
			Files.deleteIfExists(filePath);
			Files.deleteIfExists(thumbnailPath);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}*/
    
    public void deleteFile(String fileName){
	 	if(fileName == null){
	 		return;
	 	}
	 	//썸네일이 있는지 확인하고 삭제
	 	String thumbnailFileName = "s_" + fileName;
		Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
		Path filePath = Paths.get(uploadPath, fileName);

		try {
			Files.deleteIfExists(filePath);
			Files.deleteIfExists(thumbnailPath);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}

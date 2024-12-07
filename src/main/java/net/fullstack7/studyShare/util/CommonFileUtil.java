package net.fullstack7.studyShare.util;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import net.coobird.thumbnailator.Thumbnails;

public class CommonFileUtil {

    private static final String UPLOAD_DIR = "D:\\chunjae\\file";
    private static final String DELETE_DIR = "D:\\chunjae";
    @Value("${file.upload.path}")
    private String uploadPath;

    // 단일 파일 업로드 메서드
    public static String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;  // null 허용
        }
        File uploadDir = new File(UPLOAD_DIR);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String originalFileName = file.getOriginalFilename(); //업로드 된 원래 파일 이름
        if (originalFileName != null && !originalFileName.isEmpty()) {
            String uniqueFileName = generateUniqueFileName(originalFileName); //고유 파일명 생성
            String fullPath = uploadDir.getPath() + File.separator + uniqueFileName; // 파일 저장 경로 생성
            File destinationFile = new File(fullPath); // 파일 객체 생성
            file.transferTo(destinationFile); //MultipartFile의 내용을 실제 파일로 저장
            // 업로드된 파일의 경로 반환
            return uniqueFileName;
        } else {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }
    }

    //썸네일
    public static String createThumbnail(String fileName) throws IOException {
        String inputFilePath = UPLOAD_DIR + File.separator + fileName; // 원본 파일 경로
        String thumbnailName = "thumb_" + fileName; // 썸네일 파일 이름
        String outputFilePath = UPLOAD_DIR + File.separator + thumbnailName; // 썸네일 저장 경로

        Thumbnails.of(inputFilePath)
                .size(100, 100)  // 썸네일 크기
                .outputQuality(0.8) // 용량
                .outputFormat("jpg")  // 파일 형식
                .toFile(outputFilePath);  // 저장 경로
        return thumbnailName;
    }

    // 다중 파일 업로드 메서드
    public static List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> uploadedFilePaths = new ArrayList<>();
        File uploadDir = new File(UPLOAD_DIR);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            if (originalFileName != null && !originalFileName.isEmpty()) {
                String uniqueFileName = generateUniqueFileName(originalFileName);
                String fullPath = uploadDir.getPath() + File.separator + uniqueFileName;
                File destinationFile = new File(fullPath);
                file.transferTo(destinationFile);
                uploadedFilePaths.add(fullPath);
            }
        }
        return uploadedFilePaths;
    }

    // 파일 삭제 메서드
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) return false;
        String dir = DELETE_DIR+filePath;
        File file = new File(dir);
        return file.exists() && file.delete();
    }

    // 파일명 변경 메서드
    public static String renameFile(String oldFilePath, String newFileName) throws IOException {
        File oldFile = new File(oldFilePath);

        String uniqueNewFileName = generateUniqueFileName(newFileName);
        String newFullPath = UPLOAD_DIR + uniqueNewFileName;
        File newFile = new File(newFullPath);

        if (oldFile.exists() && oldFile.renameTo(newFile)) {
            return newFullPath; // 새 파일 경로 반환
        } else {
            throw new IOException("파일명을 변경할 수 없습니다.");
        }
    }

    // 고유 파일명 생성 메서드
    private static String generateUniqueFileName(String originalFileName) {
        String extension = "";
        int index = originalFileName.lastIndexOf("."); //lastIndexOf(".")를 사용해 파일 이름에서 마지막 .의 위치 찾음
        if (index > 0) {
            extension = originalFileName.substring(index); //파일 확장자 추출
        }
        return UUID.randomUUID().toString() + extension; //고유 파일명 생성
    }

    public static Resource downloadFile(String fileName) throws IOException {
        File file = new File(UPLOAD_DIR + File.separator + fileName);

        if (file.exists()) {
            return new FileSystemResource(file); // FileSystemResource는 Spring에서 파일을 읽는 데 사용됩니다.
        } else {
            throw new IOException("파일을 찾을 수 없습니다: " + fileName); // 파일이 존재하지 않으면 예외 처리
        }
    }
}

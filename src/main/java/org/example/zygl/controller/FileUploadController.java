package org.example.zygl.controller;

import lombok.RequiredArgsConstructor;
import org.example.zygl.dto.FileUploadResultDTO;
import org.example.zygl.service.FileUploadService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 * <p>
 * 提供封面、视频、文档、附件等文件的上传能力。
 * 文件上传成功后返回可访问的 URL，前端再提交资源表单。
 */
@RestController
@RequestMapping("/api/file-upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    /**
     * 上传封面图片
     */
    @PostMapping("/cover")
    public R<FileUploadResultDTO> uploadCover(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadCover(file);
        return R.ok(buildResult(url, file));
    }

    /**
     * 上传视频文件
     */
    @PostMapping("/video")
    public R<FileUploadResultDTO> uploadVideo(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadVideo(file);
        return R.ok(buildResult(url, file));
    }

    /**
     * 上传文档文件
     */
    @PostMapping("/document")
    public R<FileUploadResultDTO> uploadDocument(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadDocument(file);
        return R.ok(buildResult(url, file));
    }

    /**
     * 上传附件文件
     */
    @PostMapping("/attachment")
    public R<FileUploadResultDTO> uploadAttachment(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadAttachment(file);
        return R.ok(buildResult(url, file));
    }

    /**
     * 构建上传结果 DTO
     */
    private FileUploadResultDTO buildResult(String url, MultipartFile file) {
        FileUploadResultDTO dto = new FileUploadResultDTO();
        dto.setUrl(url);
        dto.setSize(file.getSize());
        dto.setOriginalName(file.getOriginalFilename());
        return dto;
    }
}

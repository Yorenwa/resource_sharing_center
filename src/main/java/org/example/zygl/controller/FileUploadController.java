package org.example.zygl.controller;

import lombok.RequiredArgsConstructor;
import org.example.zygl.service.FileUploadService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

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
    public R<Map<String, Object>> uploadCover(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadCover(file);
        Map<String, Object> result = new HashMap<>();
        result.put("url", url);
        result.put("size", file.getSize());
        result.put("originalName", file.getOriginalFilename());
        return R.ok(result);
    }

    /**
     * 上传视频文件
     */
    @PostMapping("/video")
    public R<Map<String, Object>> uploadVideo(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadVideo(file);
        Map<String, Object> result = new HashMap<>();
        result.put("url", url);
        result.put("size", file.getSize());
        result.put("originalName", file.getOriginalFilename());
        return R.ok(result);
    }

    /**
     * 上传文档文件
     */
    @PostMapping("/document")
    public R<Map<String, Object>> uploadDocument(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadDocument(file);
        Map<String, Object> result = new HashMap<>();
        result.put("url", url);
        result.put("size", file.getSize());
        result.put("originalName", file.getOriginalFilename());
        return R.ok(result);
    }

    /**
     * 上传附件文件
     */
    @PostMapping("/attachment")
    public R<Map<String, Object>> uploadAttachment(@RequestParam("file") MultipartFile file) {
        String url = fileUploadService.uploadAttachment(file);
        Map<String, Object> result = new HashMap<>();
        result.put("url", url);
        result.put("size", file.getSize());
        result.put("originalName", file.getOriginalFilename());
        return R.ok(result);
    }
}

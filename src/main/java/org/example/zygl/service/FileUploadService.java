package org.example.zygl.service;

import org.example.zygl.dto.ResourceUploadRequest;
import org.example.zygl.entity.ResourceManagement;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件上传服务接口
 */
public interface FileUploadService {

    /**
     * 上传封面图片
     *
     * @param file 封面文件
     * @return 访问 URL
     */
    String uploadCover(MultipartFile file);

    /**
     * 上传视频文件
     *
     * @param file 视频文件
     * @return 访问 URL
     */
    String uploadVideo(MultipartFile file);

    /**
     * 上传文档文件
     *
     * @param file 文档文件
     * @return 访问 URL
     */
    String uploadDocument(MultipartFile file);

    /**
     * 上传附件文件
     *
     * @param file 附件文件
     * @return 访问 URL
     */
    String uploadAttachment(MultipartFile file);

    /**
     * 上传文件（通用）
     *
     * @param file     文件
     * @param dir      子目录
     * @param types    允许的扩展名
     * @param maxSize  最大大小
     * @param fileType 文件类型描述
     * @return 访问 URL
     */
    String upload(MultipartFile file, String dir, String[] types, long maxSize, String fileType);
}

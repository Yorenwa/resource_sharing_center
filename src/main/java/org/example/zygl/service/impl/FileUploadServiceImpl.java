package org.example.zygl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.zygl.config.UploadConfig;
import org.example.zygl.service.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * 文件上传服务实现类
 * <p>
 * 支持封面、视频、文档、附件等多种文件类型的上传，
 * 提供类型校验、大小校验和唯一文件名生成。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final UploadConfig uploadConfig;

    @Override
    public String uploadCover(MultipartFile file) {
        return upload(file, "covers", uploadConfig.getAllowedImageTypes(),
                uploadConfig.getMaxCoverSize(), "封面图片");
    }

    @Override
    public String uploadVideo(MultipartFile file) {
        return upload(file, "videos", uploadConfig.getAllowedVideoTypes(),
                uploadConfig.getMaxVideoSize(), "视频");
    }

    @Override
    public String uploadDocument(MultipartFile file) {
        return upload(file, "documents", uploadConfig.getAllowedDocTypes(),
                uploadConfig.getMaxDocSize(), "文档");
    }

    @Override
    public String uploadAttachment(MultipartFile file) {
        return upload(file, "attachments", uploadConfig.getAllowedAttachmentTypes(),
                uploadConfig.getMaxAttachmentSize(), "附件");
    }

    /**
     * 通用文件上传实现
     * <p>
     * 执行以下步骤：
     * <ol>
     *   <li>校验文件不为空</li>
     *   <li>校验文件扩展名在允许列表中</li>
     *   <li>校验文件大小不超过上限</li>
     *   <li>生成唯一文件名并保存到磁盘</li>
     *   <li>返回可访问的 URL</li>
     * </ol>
     */
    @Override
    public String upload(MultipartFile file, String dir, String[] types,
                         long maxSize, String fileType) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(fileType + "不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);

        if (extension == null || !Arrays.asList(types).contains(extension.toLowerCase())) {
            throw new IllegalArgumentException(fileType + "格式不支持，允许的格式：" + Arrays.toString(types));
        }

        long size = file.getSize();
        if (size > maxSize) {
            throw new IllegalArgumentException(fileType + "大小超限，最大允许：" + formatSize(maxSize));
        }

        String uuid = UUID.randomUUID().toString().replace("-", "");
        String newFilename = uuid + "." + extension;

        File destDir = new File(uploadConfig.getPath(), dir);
        if (!destDir.exists() && !destDir.mkdirs()) {
            throw new RuntimeException("创建上传目录失败：" + destDir.getAbsolutePath());
        }

        File destFile = new File(destDir, newFilename);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage(), e);
            throw new RuntimeException("文件保存失败");
        }

        String url = uploadConfig.getUrlPrefix() + "/" + dir + "/" + newFilename;
        log.info("文件上传成功：type={}, size={}, url={}", fileType, size, url);
        return url;
    }

    private String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            return null;
        }
        return filename.substring(dotIndex + 1);
    }

    private String formatSize(long bytes) {
        if (bytes >= 1024 * 1024 * 1024) {
            return (bytes / (1024.0 * 1024.0 * 1024)) + "GB";
        } else if (bytes >= 1024 * 1024) {
            return (bytes / (1024.0 * 1024.0)) + "MB";
        } else if (bytes >= 1024) {
            return (bytes / 1024.0) + "KB";
        }
        return bytes + "B";
    }
}

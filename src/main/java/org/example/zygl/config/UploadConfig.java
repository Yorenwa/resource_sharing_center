package org.example.zygl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "upload")
public class UploadConfig {

    /**
     * 文件存储根目录
     */
    private String path = "./uploads";

    /**
     * 访问 URL 前缀
     */
    private String urlPrefix = "/uploads";

    /**
     * 允许的视频文件类型
     */
    private String[] allowedVideoTypes = {"mp4"};

    /**
     * 允许的文档文件类型
     */
    private String[] allowedDocTypes = {"pdf"};

    /**
     * 允许的附件文件类型
     */
    private String[] allowedAttachmentTypes = {"ppt", "pptx", "xls", "xlsx", "doc", "docx", "pdf"};

    /**
     * 允许的图片类型（封面）
     */
    private String[] allowedImageTypes = {"jpg", "jpeg", "png", "gif", "webp"};

    /**
     * 视频最大大小（字节）：500MB
     */
    private long maxVideoSize = 500L * 1024 * 1024;

    /**
     * 文档最大大小（字节）：50MB
     */
    private long maxDocSize = 50L * 1024 * 1024;

    /**
     * 附件最大大小（字节）：50MB
     */
    private long maxAttachmentSize = 50L * 1024 * 1024;

    /**
     * 封面最大大小（字节）：5MB
     */
    private long maxCoverSize = 5L * 1024 * 1024;
}

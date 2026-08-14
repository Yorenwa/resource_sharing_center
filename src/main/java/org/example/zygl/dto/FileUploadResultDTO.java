package org.example.zygl.dto;

import lombok.Data;

/**
 * 文件上传结果 DTO
 * <p>
 * 统一封装文件上传成功后的返回信息，替代 Map 结构。
 */
@Data
public class FileUploadResultDTO {

    /**
     * 文件访问 URL
     */
    private String url;

    /**
     * 文件大小（字节）
     */
    private Long size;

    /**
     * 原始文件名
     */
    private String originalName;
}

package org.example.zygl.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 资源上传请求体（视频/文档通用）
 * <p>
 * 对应前端"上传资源"页面的表单提交。
 */
@Data
public class ResourceUploadRequest {

    /**
     * 资源类型：1-视频，2-文档
     */
    @NotNull(message = "资源类型不能为空")
    private Integer resourceType;

    /**
     * 资源封面 URL（视频必填）
     */
    private String coverUrl;

    /**
     * 学段
     */
    private String schoolStage;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类路径
     */
    private String categoryPath;

    /**
     * 资源标签（逗号分隔，最多5个）
     */
    private String resourceTag;

    /**
     * 参与范围：1-公开，2-指定范围
     */
    private Integer scope;

    /**
     * 上传人
     */
    private String uploader;

    /**
     * 学校ID
     */
    private Long schoolUid;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 资源明细列表（至少一项）
     */
    @NotEmpty(message = "资源明细不能为空")
    @Valid
    private List<ResourceDetailUploadItem> details;

    @Data
    public static class ResourceDetailUploadItem {

        /**
         * 集号（从1开始）
         */
        private Integer episodeNo;

        /**
         * 资源文件 URL（视频：MP4；文档：PDF）
         */
        private String resourceFileUrl;

        /**
         * 资源文件存储大小（字节）
         */
        private Long resourceStorage;

        /**
         * 资源名称
         */
        private String episodeName;

        /**
         * 资源描述
         */
        private String description;

        /**
         * 附件 URL
         */
        private String attachmentUrl;

        /**
         * 附件名称
         */
        private String attachmentName;

        /**
         * 附件存储大小（字节）
         */
        private Long attachmentStorage;
    }
}

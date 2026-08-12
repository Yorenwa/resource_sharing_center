package org.example.zygl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("resource_management")
public class ResourceManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pk_id", type = IdType.AUTO)
    private Long pkId;

    @TableField("resource_name")
    private String resourceName;

    @TableField("resource_url")
    private String resourceUrl;

    @TableField("resource_type")
    private Integer resourceType;

    @TableField("cover_url")
    private String coverUrl;

    @TableField("school_stage")
    private String schoolStage;

    @TableField("category_id")
    private Long categoryId;

    @TableField("category_path")
    private String categoryPath;

    @TableField("resource_tag")
    private String resourceTag;

    @TableField("scope")
    private Integer scope;

    @TableField("uploader")
    private String uploader;

    @TableField("school_uid")
    private Long schoolUid;

    @TableField("school_name")
    private String schoolName;

    @TableField("status")
    private Integer status;

    @TableField("rejection_reason")
    private String rejectionReason;

    @TableField("recommended")
    private Integer recommended;

    @TableField("pinned")
    private Integer pinned;

    @TableField("clicks")
    private Integer clicks;

    @TableField("creator")
    private String creator;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("updater")
    private String updater;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("tenant_id")
    private String tenantId;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}

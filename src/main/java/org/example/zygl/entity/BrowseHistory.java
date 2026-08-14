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
@TableName("browse_history")
public class BrowseHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pk_id", type = IdType.AUTO)
    private Long pkId;

    @TableField("user_id")
    private Long userId;

    @TableField("resource_id")
    private Long resourceId;

    @TableField("resource_name")
    private String resourceName;

    @TableField("resource_history_url")
    private String resourceHistoryUrl;

    @TableField("resource_type")
    private Integer resourceType;

    @TableField("category_id")
    private Long categoryId;

    @TableField("category_path")
    private String categoryPath;

    /**
     * 分类名称路径（瞬态字段，由 Service 层填充，如 "区级安全宣传/安全制度"）
     */
    @TableField(exist = false)
    private String categoryNamePath;

    @TableField("browse_time")
    private LocalDateTime browseTime;

    @TableField("tenant_id")
    private String tenantId;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}

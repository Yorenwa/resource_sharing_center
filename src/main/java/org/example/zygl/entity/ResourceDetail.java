package org.example.zygl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("resource_detail")
public class ResourceDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pk_id", type = IdType.AUTO)
    private Long pkId;

    @TableField("resource_id")
    private Long resourceId;

    @TableField("episode_no")
    private Integer episodeNo;

    @TableField("resource_file_url")
    private String resourceFileUrl;

    @TableField("episode_name")
    private String episodeName;

    @TableField("resource_storage")
    private Long resourceStorage;

    @TableField("description")
    private String description;

    @TableField("attachment_url")
    private String attachmentUrl;

    @TableField("attachment_name")
    private String attachmentName;

    @TableField("attachment_storage")
    private Long attachmentStorage;

    @TableField("tenant_id")
    private String tenantId;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}

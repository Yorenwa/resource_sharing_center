package org.example.zygl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("cloud_storage")
public class CloudStorage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pk_id", type = IdType.AUTO)
    private Long pkId;

    @TableField("total_storage")
    private Long totalStorage;

    @TableField("used_storage")
    private Long usedStorage;

    @TableField("doc_used_storage")
    private Long docUsedStorage;

    @TableField("video_used_storage")
    private Long videoUsedStorage;

    @TableField("usage_rate")
    private BigDecimal usageRate;

    @TableField("tenant_id")
    private String tenantId;
}

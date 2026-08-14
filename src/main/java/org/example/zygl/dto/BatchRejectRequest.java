package org.example.zygl.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 批量审核驳回请求体
 */
@Data
public class BatchRejectRequest {

    /**
     * 资源ID列表
     */
    @NotEmpty(message = "ids不能为空")
    private List<Long> ids;

    /**
     * 驳回原因
     */
    private String rejectionReason;
}

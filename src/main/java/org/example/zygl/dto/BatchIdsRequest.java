package org.example.zygl.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 批量ID请求体（通用）
 * <p>
 * 用于批量删除、批量操作等场景。
 */
@Data
public class BatchIdsRequest {

    /**
     * ID列表
     */
    @NotEmpty(message = "ids不能为空")
    private List<Long> ids;
}

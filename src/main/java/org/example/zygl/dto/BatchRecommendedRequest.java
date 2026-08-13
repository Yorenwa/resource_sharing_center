package org.example.zygl.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量设置推荐状态请求体
 */
@Data
public class BatchRecommendedRequest {

    /**
     * 资源ID列表
     */
    @NotEmpty(message = "ids不能为空")
    private List<Long> ids;

    /**
     * 推荐状态：1-推荐，0-取消推荐
     */
    @NotNull(message = "recommended不能为空")
    private Integer recommended;
}

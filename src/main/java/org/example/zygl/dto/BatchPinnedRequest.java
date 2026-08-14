package org.example.zygl.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量设置置顶状态请求体
 */
@Data
public class BatchPinnedRequest {

    /**
     * 资源ID列表
     */
    @NotEmpty(message = "ids不能为空")
    private List<Long> ids;

    /**
     * 置顶状态：1-置顶，0-取消置顶
     */
    @NotNull(message = "pinned不能为空")
    private Integer pinned;
}

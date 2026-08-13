package org.example.zygl.dto;

import lombok.Data;

/**
 * 分类资源统计 DTO
 */
@Data
public class CategoryStatDTO {

    /**
     * 全部资源数
     */
    private Integer total;

    /**
     * 置顶资源数
     */
    private Integer pinned;

    /**
     * 推荐资源数
     */
    private Integer recommended;
}

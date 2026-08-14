package org.example.zygl.dto;

import lombok.Data;

/**
 * 轮播图统计 DTO
 * <p>
 * 返回总数、启用数及系统配置上限，供前端判断是否允许新增或启用操作。
 */
@Data
public class CarouselStatsDTO {

    /**
     * 当前总数
     */
    private Integer total;

    /**
     * 启用数
     */
    private Integer enabled;

    /**
     * 启用上限
     */
    private Integer maxEnabled;

    /**
     * 总数上限
     */
    private Integer maxTotal;
}

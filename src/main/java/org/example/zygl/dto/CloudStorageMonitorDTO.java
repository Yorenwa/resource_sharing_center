package org.example.zygl.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 云空间状态监测 DTO
 * <p>
 * 对应"云空间管理"页面顶部的两个监测卡片：
 * <ul>
 *   <li>空间使用量：总容量、已用空间、剩余空间、使用率</li>
 *   <li>资源类型分布：视频资料占用、文档资料占用（饼图数据）</li>
 * </ul>
 * 所有存储量字段单位为 GB，保留 2 位小数，便于前端直接展示。
 */
@Data
public class CloudStorageMonitorDTO {

    /**
     * 总容量（GB）
     */
    private BigDecimal totalStorageGb;

    /**
     * 已用空间（GB）
     */
    private BigDecimal usedStorageGb;

    /**
     * 剩余空间（GB）
     */
    private BigDecimal remainingStorageGb;

    /**
     * 使用率（百分比，0-100，保留 2 位小数）
     */
    private BigDecimal usageRate;

    /**
     * 视频资料占用（GB）
     */
    private BigDecimal videoUsedGb;

    /**
     * 文档资料占用（GB）
     */
    private BigDecimal docUsedGb;
}

package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.dto.CloudStorageMonitorDTO;
import org.example.zygl.entity.CloudStorage;
import org.example.zygl.enums.ResourceTypeEnum;
import org.example.zygl.mapper.CloudStorageMapper;
import org.example.zygl.mapper.ResourceDetailMapper;
import org.example.zygl.service.CloudStorageService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 云空间服务实现类
 * <p>
 * 核心逻辑：
 * <ul>
 *   <li>总容量：优先从 cloud_storage 配置表读取，未配置则使用默认 100GB</li>
 *   <li>已用空间：实时聚合 resource_detail 的 resource_storage + attachment_storage</li>
 *   <li>使用率：已用 / 总容量 × 100，保留 2 位小数</li>
 *   <li>类型分布：分别统计视频（1）和文档（2）的存储占用</li>
 * </ul>
 */
@Service
public class CloudStorageServiceImpl
        extends ServiceImpl<CloudStorageMapper, CloudStorage>
        implements CloudStorageService {

    /**
     * 1 GB = 1024^3 字节
     */
    private static final long BYTES_PER_GB = 1024L * 1024L * 1024L;

    /**
     * 默认总容量（字节）= 100 GB
     */
    private static final long DEFAULT_TOTAL_BYTES = 100L * BYTES_PER_GB;

    private final ResourceDetailMapper resourceDetailMapper;

    public CloudStorageServiceImpl(ResourceDetailMapper resourceDetailMapper) {
        this.resourceDetailMapper = resourceDetailMapper;
    }

    @Override
    public CloudStorage getByTenantId(String tenantId) {
        return baseMapper.selectByTenantId(tenantId);
    }

    @Override
    public CloudStorageMonitorDTO monitor(String tenantId) {
        // 1. 获取总容量（配置值 or 默认 100GB）
        long totalBytes = DEFAULT_TOTAL_BYTES;
        if (tenantId != null && !tenantId.isEmpty()) {
            CloudStorage storage = baseMapper.selectByTenantId(tenantId);
            if (storage != null && storage.getTotalStorage() != null) {
                totalBytes = storage.getTotalStorage();
            }
        }

        // 2. 按资源类型分别聚合明细存储占用
        Long videoBytes = resourceDetailMapper.sumStorageByResourceType(
                ResourceTypeEnum.VIDEO.getCode(), tenantId);
        Long docBytes = resourceDetailMapper.sumStorageByResourceType(
                ResourceTypeEnum.DOCUMENT.getCode(), tenantId);
        videoBytes = (videoBytes == null) ? 0L : videoBytes;
        docBytes = (docBytes == null) ? 0L : docBytes;
        long usedBytes = videoBytes + docBytes;

        // 3. 计算剩余
        long remainingBytes = Math.max(totalBytes - usedBytes, 0L);

        // 4. 单位换算：字节 → GB（保留 2 位小数）
        BigDecimal totalGb = bytesToGb(totalBytes);
        BigDecimal usedGb = bytesToGb(usedBytes);
        BigDecimal remainingGb = bytesToGb(remainingBytes);
        BigDecimal videoGb = bytesToGb(videoBytes);
        BigDecimal docGb = bytesToGb(docBytes);

        // 5. 使用率 = 已用 / 总容量 × 100
        BigDecimal usageRate = BigDecimal.ZERO;
        if (totalBytes > 0) {
            usageRate = BigDecimal.valueOf(usedBytes)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalBytes), 2, RoundingMode.HALF_UP);
        }

        CloudStorageMonitorDTO dto = new CloudStorageMonitorDTO();
        dto.setTotalStorageGb(totalGb);
        dto.setUsedStorageGb(usedGb);
        dto.setRemainingStorageGb(remainingGb);
        dto.setUsageRate(usageRate);
        dto.setVideoUsedGb(videoGb);
        dto.setDocUsedGb(docGb);
        return dto;
    }

    /**
     * 字节转 GB（保留 2 位小数，四舍五入）
     */
    private BigDecimal bytesToGb(long bytes) {
        return BigDecimal.valueOf(bytes)
                .divide(BigDecimal.valueOf(BYTES_PER_GB), 2, RoundingMode.HALF_UP);
    }
}

package org.example.zygl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.zygl.dto.CloudStorageMonitorDTO;
import org.example.zygl.entity.CloudStorage;

/**
 * 云空间服务接口
 * <p>
 * 对应"云空间管理"页面，核心能力：
 * <ul>
 *   <li>按租户获取云空间配置（总容量等）</li>
 *   <li>实时监测：空间使用量（总/已用/剩余/使用率）+ 资源类型分布（视频/文档）</li>
 * </ul>
 */
public interface CloudStorageService extends IService<CloudStorage> {

    CloudStorage getByTenantId(String tenantId);

    /**
     * 云空间状态监测
     * <p>
     * 聚合 resource_detail 表的实际存储占用，按类型计算视频/文档空间，
     * 结果以 GB 为单位（保留 1 位小数）返回，前端可直接渲染卡片和饼图。
     *
     * @param tenantId 租户ID（可选，空则统计全量）
     * @return 监测数据 DTO
     */
    CloudStorageMonitorDTO monitor(String tenantId);
}

package org.example.zygl.controller;

import lombok.RequiredArgsConstructor;
import org.example.zygl.dto.CloudStorageMonitorDTO;
import org.example.zygl.entity.CloudStorage;
import org.example.zygl.service.CloudStorageService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 云空间管理接口
 * <p>
 * 对应前端"云空间管理"页面，提供：
 * <ul>
 *   <li>云空间状态监测（空间使用量卡片 + 资源类型分布饼图）</li>
 *   <li>配置查看（总容量、已用量、使用率等）</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/cloud-storage")
@RequiredArgsConstructor
public class CloudStorageController {

    private final CloudStorageService cloudStorageService;

    /**
     * 云空间状态监测
     * <p>
     * 对应页面顶部的"云空间状态监测"区域：
     * - 空间使用量卡片：总容量、已用空间、剩余空间、使用率
     * - 资源类型分布饼图：视频资料占用、文档资料占用
     *
     * @param tenantId 租户ID（可选，空则统计全量）
     */
    @GetMapping("/monitor")
    public R<CloudStorageMonitorDTO> monitor(
            @RequestParam(required = false) String tenantId) {
        CloudStorageMonitorDTO result = cloudStorageService.monitor(tenantId);
        return R.ok(result);
    }

    @GetMapping("/list")
    public R<List<CloudStorage>> list() {
        return R.ok(cloudStorageService.list());
    }

    @GetMapping("/{id}")
    public R<CloudStorage> getById(@PathVariable Long id) {
        CloudStorage entity = cloudStorageService.getById(id);
        if (entity == null) {
            return R.fail("云空间记录不存在");
        }
        return R.ok(entity);
    }

    @GetMapping("/tenant/{tenantId}")
    public R<CloudStorage> getByTenantId(@PathVariable String tenantId) {
        CloudStorage entity = cloudStorageService.getByTenantId(tenantId);
        if (entity == null) {
            return R.fail("云空间记录不存在");
        }
        return R.ok(entity);
    }
}

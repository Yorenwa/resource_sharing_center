package org.example.zygl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.zygl.entity.PortalCarousel;
import org.example.zygl.service.PortalCarouselService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门户轮播图管理接口
 * <p>
 * 业务约束：轮播图总数上限 10 个，启用中上限 6 个。
 * 新增和启用操作会触发数量校验，校验失败时返回失败响应（code=400）及提示信息。
 */
@RestController
@RequestMapping("/api/portal-carousel")
@RequiredArgsConstructor
public class PortalCarouselController {

    private final PortalCarouselService portalCarouselService;

    @GetMapping("/page")
    public R<IPage<PortalCarousel>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        IPage<PortalCarousel> result = portalCarouselService.pageByCondition(current, size, status);
        return R.ok(result);
    }

    @GetMapping("/list")
    public R<List<PortalCarousel>> list(@RequestParam(required = false) Integer status) {
        List<PortalCarousel> list;
        if (status != null) {
            list = portalCarouselService.listByStatus(status);
        } else {
            list = portalCarouselService.list();
        }
        return R.ok(list);
    }

    @GetMapping("/{id}")
    public R<PortalCarousel> getById(@PathVariable Long id) {
        PortalCarousel entity = portalCarouselService.getById(id);
        if (entity == null) {
            return R.fail("轮播图不存在");
        }
        return R.ok(entity);
    }

    /**
     * 查询轮播图统计信息
     * <p>
     * 返回当前总数、启用数及系统配置的上限值，供前端判断是否允许新增或启用操作。
     *
     * @return 统计信息：total(总数)、enabled(启用数)、maxEnabled(启用上限)、maxTotal(总数上限)
     */
    @GetMapping("/stats")
    public R<Map<String, Integer>> stats() {
        Map<String, Integer> result = new HashMap<>();
        result.put("total", portalCarouselService.countAll());
        result.put("enabled", portalCarouselService.countByStatus(1));
        result.put("maxEnabled", PortalCarouselService.MAX_ENABLED);
        result.put("maxTotal", PortalCarouselService.MAX_TOTAL);
        return R.ok(result);
    }

    /**
     * 新增轮播图
     * <p>
     * 保存前会校验总数上限（10个）和启用数上限（6个），校验失败返回错误信息。
     *
     * @param entity 轮播图实体（需包含 image、status 等字段）
     * @return 操作结果
     */
    @PostMapping
    public R<Boolean> save(@RequestBody PortalCarousel entity) {
        try {
            portalCarouselService.validateBeforeSave(entity.getStatus());
        } catch (RuntimeException e) {
            return R.fail(e.getMessage());
        }
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        boolean result = portalCarouselService.save(entity);
        return R.ok(result);
    }

    /**
     * 批量新增轮播图
     * <p>
     * 逐条校验业务规则，只要有一条不通过则全部拒绝，避免部分成功导致数据不一致。
     *
     * @param entities 轮播图列表
     * @return 操作结果
     */
    @PostMapping("/batch")
    public R<Boolean> saveBatch(@RequestBody List<PortalCarousel> entities) {
        for (PortalCarousel e : entities) {
            try {
                portalCarouselService.validateBeforeSave(e.getStatus());
            } catch (RuntimeException ex) {
                return R.fail(ex.getMessage());
            }
            e.setCreateTime(LocalDateTime.now());
            e.setUpdateTime(LocalDateTime.now());
        }
        boolean result = portalCarouselService.saveBatch(entities);
        return R.ok(result);
    }

    @PutMapping
    public R<Boolean> update(@RequestBody PortalCarousel entity) {
        entity.setUpdateTime(LocalDateTime.now());
        boolean result = portalCarouselService.updateById(entity);
        return R.ok(result);
    }

    /**
     * 设置轮播图状态（启用/停用）
     * <p>
     * 设置为启用（status=1）时校验启用数上限，超限则返回错误。
     *
     * @param id     轮播图主键ID
     * @param status 目标状态：1-启用，0-停用
     * @return 操作结果
     */
    @PutMapping("/status/{id}")
    public R<Boolean> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            boolean result = portalCarouselService.updateStatus(id, status);
            return R.ok(result);
        } catch (RuntimeException e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 切换轮播图状态（启用↔停用一键切换）
     * <p>
     * 内部逻辑：查询当前状态取反后调用 {@link #updateStatus(Long, Integer)}，
     * 切换为启用时同样受启用数上限约束。
     *
     * @param id 轮播图主键ID
     * @return 操作结果
     */
    @PutMapping("/toggle/{id}")
    public R<Boolean> toggleStatus(@PathVariable Long id) {
        try {
            boolean result = portalCarouselService.toggleStatus(id);
            return R.ok(result);
        } catch (RuntimeException e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 批量更新轮播图排序
     * <p>
     * 前端提交需要调整排序的轮播图列表（含 pkId 和目标 order 值），
     * 后端使用 CASE WHEN 语法一次性更新，保证原子性。
     *
     * @param list 轮播图排序列表
     * @return 操作结果
     */
    @PutMapping("/sort")
    public R<Boolean> updateOrderBatch(@RequestBody List<PortalCarousel> list) {
        if (list == null || list.isEmpty()) {
            return R.fail("排序数据不能为空");
        }
        boolean result = portalCarouselService.updateOrderBatch(list);
        return R.ok(result);
    }

    @DeleteMapping("/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        boolean result = portalCarouselService.removeById(id);
        return R.ok(result);
    }

    @DeleteMapping("/batch")
    public R<Boolean> removeBatch(@RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("ids");
        if (ids == null || ids.isEmpty()) {
            return R.fail("ids不能为空");
        }
        boolean result = portalCarouselService.removeByIds(ids);
        return R.ok(result);
    }
}

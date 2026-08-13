package org.example.zygl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.zygl.dto.BatchIdsRequest;
import org.example.zygl.dto.CategoryStatDTO;
import org.example.zygl.dto.BatchPinnedRequest;
import org.example.zygl.dto.BatchRecommendedRequest;
import org.example.zygl.dto.BatchRejectRequest;
import org.example.zygl.entity.ResourceManagement;
import org.example.zygl.service.ResourceManagementService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 资源管理接口
 * <p>
 * 对应前端"资源管理"页面，提供：
 * <ul>
 *   <li>多条件分页查询（资源名称、类型、一级/二级分类、上传人、学校、状态等）</li>
 *   <li>分类统计信息（全部资源数、置顶数、推荐数）</li>
 *   <li>推荐/置顶状态切换（含置顶上限校验：每个一级分类最多4个）</li>
 *   <li>审核操作（通过/驳回/批量审核）</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/resource-management")
@RequiredArgsConstructor
public class ResourceManagementController {

    private final ResourceManagementService resourceManagementService;

    /**
     * 条件分页查询
     * <p>
     * 对应页面顶部的过滤条件区域。
     *
     * @param current         当前页
     * @param size            每页大小
     * @param resourceName    资源名称（模糊查询）
     * @param resourceType    资源类型
     * @param categoryId      分类ID（精确匹配）
     * @param level1CategoryId 一级分类ID（含子分类）
     * @param level2CategoryId 二级分类ID
     * @param status          审核状态
     * @param scope           参与范围
     * @param recommended     是否推荐
     * @param pinned          是否置顶
     * @param uploader        上传人（模糊查询）
     * @param schoolUid       学校ID
     */
    @GetMapping("/page")
    public R<IPage<ResourceManagement>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String resourceName,
            @RequestParam(required = false) Integer resourceType,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long level1CategoryId,
            @RequestParam(required = false) Long level2CategoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer scope,
            @RequestParam(required = false) Integer recommended,
            @RequestParam(required = false) Integer pinned,
            @RequestParam(required = false) String uploader,
            @RequestParam(required = false) Long schoolUid) {
        IPage<ResourceManagement> result = resourceManagementService.pageByCondition(
                current, size, resourceName, resourceType, categoryId,
                level1CategoryId, level2CategoryId, status, scope,
                recommended, pinned, uploader, schoolUid);
        return R.ok(result);
    }

    @GetMapping("/list")
    public R<List<ResourceManagement>> list() {
        return R.ok(resourceManagementService.list());
    }

    @GetMapping("/{id}")
    public R<ResourceManagement> getById(@PathVariable Long id) {
        ResourceManagement entity = resourceManagementService.getById(id);
        if (entity == null) {
            return R.fail("资源不存在");
        }
        return R.ok(entity);
    }

    /**
     * 分类统计信息
     * <p>
     * 返回指定分类下的全部资源数、置顶资源数、推荐资源数。
     * 对应页面顶部的三个统计卡片。
     *
     * @param categoryPath 分类路径（可选）
     */
    @GetMapping("/stats")
    public R<CategoryStatDTO> stats(
            @RequestParam(required = false) String categoryPath) {
        CategoryStatDTO result = resourceManagementService.categoryStats(categoryPath);
        return R.ok(result);
    }

    @GetMapping("/category/{categoryId}")
    public R<List<ResourceManagement>> listByCategoryId(@PathVariable Long categoryId) {
        return R.ok(resourceManagementService.listByCategoryId(categoryId));
    }

    @GetMapping("/uploader/{uploader}")
    public R<List<ResourceManagement>> listByUploader(@PathVariable String uploader) {
        return R.ok(resourceManagementService.listByUploader(uploader));
    }

    @GetMapping("/recommended")
    public R<List<ResourceManagement>> listRecommended() {
        return R.ok(resourceManagementService.listRecommended());
    }

    @GetMapping("/pinned")
    public R<List<ResourceManagement>> listPinned() {
        return R.ok(resourceManagementService.listPinned());
    }

    /**
     * 新增资源
     */
    @PostMapping
    public R<Boolean> save(@RequestBody ResourceManagement entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        boolean result = resourceManagementService.save(entity);
        return R.ok(result);
    }

    @PostMapping("/batch")
    public R<Boolean> saveBatch(@RequestBody List<ResourceManagement> entities) {
        entities.forEach(e -> {
            e.setCreateTime(LocalDateTime.now());
            e.setUpdateTime(LocalDateTime.now());
        });
        boolean result = resourceManagementService.saveBatch(entities);
        return R.ok(result);
    }

    @PutMapping
    public R<Boolean> update(@RequestBody ResourceManagement entity) {
        entity.setUpdateTime(LocalDateTime.now());
        boolean result = resourceManagementService.updateById(entity);
        return R.ok(result);
    }

    /**
     * 资源点击量自增
     */
    @PutMapping("/click/{id}")
    public R<Boolean> addClick(@PathVariable Long id) {
        boolean result = resourceManagementService.addClick(id);
        return R.ok(result);
    }

    /**
     * 设置推荐状态
     * <p>
     * 对应"推荐/取消推荐"操作。
     *
     * @param id          资源ID
     * @param recommended 1-推荐，0-取消推荐
     */
    @PutMapping("/recommended/{id}")
    public R<Boolean> updateRecommended(@PathVariable Long id, @RequestParam Integer recommended) {
        boolean result = resourceManagementService.updateRecommended(id, recommended);
        return R.ok(result);
    }

    /**
     * 切换推荐状态（一键切换）
     */
    @PutMapping("/recommended/toggle/{id}")
    public R<Boolean> toggleRecommended(@PathVariable Long id) {
        boolean result = resourceManagementService.toggleRecommended(id);
        return R.ok(result);
    }

    /**
     * 设置置顶状态（含置顶上限校验）
     * <p>
     * 单个一级分类下最多 4 个置顶资源，超上限返回错误。
     *
     * @param id     资源ID
     * @param pinned 1-置顶，0-取消置顶
     */
    @PutMapping("/pinned/{id}")
    public R<Boolean> updatePinned(@PathVariable Long id, @RequestParam Integer pinned) {
        try {
            boolean result = resourceManagementService.updatePinned(id, pinned);
            return R.ok(result);
        } catch (RuntimeException e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 切换置顶状态（一键切换）
     */
    @PutMapping("/pinned/toggle/{id}")
    public R<Boolean> togglePinned(@PathVariable Long id) {
        try {
            boolean result = resourceManagementService.togglePinned(id);
            return R.ok(result);
        } catch (RuntimeException e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 批量设置推荐状态
     */
    @PutMapping("/recommended/batch")
    public R<Boolean> batchUpdateRecommended(@Valid @RequestBody BatchRecommendedRequest request) {
        boolean result = resourceManagementService.batchUpdateRecommended(request.getIds(), request.getRecommended());
        return R.ok(result);
    }

    /**
     * 批量设置置顶状态（含置顶上限校验）
     */
    @PutMapping("/pinned/batch")
    public R<Boolean> batchUpdatePinned(@Valid @RequestBody BatchPinnedRequest request) {
        try {
            boolean result = resourceManagementService.batchUpdatePinned(request.getIds(), request.getPinned());
            return R.ok(result);
        } catch (RuntimeException e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 审核通过
     */
    @PutMapping("/approve/{id}")
    public R<Boolean> approve(@PathVariable Long id) {
        boolean result = resourceManagementService.approve(id);
        return R.ok(result);
    }

    /**
     * 审核驳回
     */
    @PutMapping("/reject/{id}")
    public R<Boolean> reject(@PathVariable Long id,
                               @RequestParam(required = false) String rejectionReason) {
        boolean result = resourceManagementService.reject(id, rejectionReason);
        return R.ok(result);
    }

    /**
     * 批量审核通过
     */
    @PutMapping("/approve/batch")
    public R<Boolean> batchApprove(@Valid @RequestBody BatchIdsRequest request) {
        boolean result = resourceManagementService.batchApprove(request.getIds());
        return R.ok(result);
    }

    /**
     * 批量审核驳回
     */
    @PutMapping("/reject/batch")
    public R<Boolean> batchReject(@Valid @RequestBody BatchRejectRequest request) {
        boolean result = resourceManagementService.batchReject(request.getIds(), request.getRejectionReason());
        return R.ok(result);
    }

    @DeleteMapping("/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        boolean result = resourceManagementService.removeById(id);
        return R.ok(result);
    }

    @DeleteMapping("/batch")
    public R<Boolean> removeBatch(@Valid @RequestBody BatchIdsRequest request) {
        boolean result = resourceManagementService.removeByIds(request.getIds());
        return R.ok(result);
    }
}

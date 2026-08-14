package org.example.zygl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.zygl.dto.BatchIdsRequest;
import org.example.zygl.entity.BrowseHistory;
import org.example.zygl.service.BrowseHistoryService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 浏览记录接口
 * <p>
 * 对应前端"浏览记录"页面：
 * <ul>
 *   <li>多条件分页查询（资源名称、资源类型、分类路径过滤）</li>
 *   <li>单条/批量删除</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/browse-history")
@RequiredArgsConstructor
public class BrowseHistoryController {

    private final BrowseHistoryService browseHistoryService;

    /**
     * 条件分页查询
     * <p>
     * 对应页面顶部的过滤条件区域（资源名称、资源类型、分类）。
     *
     * @param current      当前页
     * @param size         每页大小
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @param categoryPath 分类路径（按路径前缀匹配，包含子分类）
     * @param resourceName 资源名称（模糊匹配）
     */
    @GetMapping("/page")
    public R<IPage<BrowseHistory>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer resourceType,
            @RequestParam(required = false) String categoryPath,
            @RequestParam(required = false) String resourceName) {
        IPage<BrowseHistory> result = browseHistoryService.pageByCondition(
                current, size, userId, resourceType, categoryPath, resourceName);
        return R.ok(result);
    }

    @GetMapping("/list")
    public R<List<BrowseHistory>> list() {
        return R.ok(browseHistoryService.list());
    }

    @GetMapping("/{id}")
    public R<BrowseHistory> getById(@PathVariable Long id) {
        BrowseHistory entity = browseHistoryService.getById(id);
        if (entity == null) {
            return R.fail("浏览记录不存在");
        }
        return R.ok(entity);
    }

    @GetMapping("/user/{userId}")
    public R<List<BrowseHistory>> listByUserId(@PathVariable Long userId) {
        return R.ok(browseHistoryService.listByUserId(userId));
    }

    @GetMapping("/resource/{resourceId}")
    public R<List<BrowseHistory>> listByResourceId(@PathVariable Long resourceId) {
        return R.ok(browseHistoryService.listByResourceId(resourceId));
    }

    @DeleteMapping("/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        boolean result = browseHistoryService.removeById(id);
        return R.ok(result);
    }

    @DeleteMapping("/batch")
    public R<Boolean> removeBatch(@Valid @RequestBody BatchIdsRequest request) {
        boolean result = browseHistoryService.removeByIds(request.getIds());
        return R.ok(result);
    }
}

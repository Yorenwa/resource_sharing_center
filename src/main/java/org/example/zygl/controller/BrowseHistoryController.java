package org.example.zygl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.zygl.entity.BrowseHistory;
import org.example.zygl.service.BrowseHistoryService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/browse-history")
@RequiredArgsConstructor
public class BrowseHistoryController {

    private final BrowseHistoryService browseHistoryService;

    @GetMapping("/page")
    public R<IPage<BrowseHistory>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer resourceType,
            @RequestParam(required = false) Long categoryId) {
        IPage<BrowseHistory> result = browseHistoryService.pageByCondition(current, size, userId, resourceType, categoryId);
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

    @PostMapping
    public R<Boolean> save(@RequestBody BrowseHistory entity) {
        entity.setBrowseTime(LocalDateTime.now());
        boolean result = browseHistoryService.save(entity);
        return R.ok(result);
    }

    @PostMapping("/batch")
    public R<Boolean> saveBatch(@RequestBody List<BrowseHistory> entities) {
        entities.forEach(e -> e.setBrowseTime(LocalDateTime.now()));
        boolean result = browseHistoryService.saveBatch(entities);
        return R.ok(result);
    }

    @PutMapping
    public R<Boolean> update(@RequestBody BrowseHistory entity) {
        boolean result = browseHistoryService.updateById(entity);
        return R.ok(result);
    }

    @DeleteMapping("/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        boolean result = browseHistoryService.removeById(id);
        return R.ok(result);
    }

    @DeleteMapping("/batch")
    public R<Boolean> removeBatch(@RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("ids");
        if (ids == null || ids.isEmpty()) {
            return R.fail("ids不能为空");
        }
        boolean result = browseHistoryService.removeByIds(ids);
        return R.ok(result);
    }
}

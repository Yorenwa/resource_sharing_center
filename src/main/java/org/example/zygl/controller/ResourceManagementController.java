package org.example.zygl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.zygl.entity.ResourceManagement;
import org.example.zygl.service.ResourceManagementService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resource-management")
@RequiredArgsConstructor
public class ResourceManagementController {

    private final ResourceManagementService resourceManagementService;

    @GetMapping("/page")
    public R<IPage<ResourceManagement>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String resourceName,
            @RequestParam(required = false) Integer resourceType,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer scope,
            @RequestParam(required = false) Integer recommended,
            @RequestParam(required = false) Integer pinned) {
        IPage<ResourceManagement> result = resourceManagementService.pageByCondition(
                current, size, resourceName, resourceType, categoryId, status, scope, recommended, pinned);
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

    @PutMapping("/click/{id}")
    public R<Boolean> addClick(@PathVariable Long id) {
        boolean result = resourceManagementService.addClick(id);
        return R.ok(result);
    }

    @DeleteMapping("/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        boolean result = resourceManagementService.removeById(id);
        return R.ok(result);
    }

    @DeleteMapping("/batch")
    public R<Boolean> removeBatch(@RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("ids");
        if (ids == null || ids.isEmpty()) {
            return R.fail("ids不能为空");
        }
        boolean result = resourceManagementService.removeByIds(ids);
        return R.ok(result);
    }
}

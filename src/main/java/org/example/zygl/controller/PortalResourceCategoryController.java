package org.example.zygl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.zygl.entity.PortalResourceCategory;
import org.example.zygl.service.PortalResourceCategoryService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resource-category")
@RequiredArgsConstructor
public class PortalResourceCategoryController {

    private final PortalResourceCategoryService portalResourceCategoryService;

    @GetMapping("/page")
    public R<IPage<PortalResourceCategory>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String typeName,
            @RequestParam(required = false) Integer status) {
        IPage<PortalResourceCategory> result = portalResourceCategoryService.pageByCondition(current, size, type, typeName, status);
        return R.ok(result);
    }

    @GetMapping("/list")
    public R<List<PortalResourceCategory>> list(@RequestParam(required = false) Integer type) {
        List<PortalResourceCategory> list;
        if (type != null) {
            list = portalResourceCategoryService.listByType(type);
        } else {
            list = portalResourceCategoryService.list();
        }
        return R.ok(list);
    }

    @GetMapping("/{id}")
    public R<PortalResourceCategory> getById(@PathVariable Long id) {
        PortalResourceCategory category = portalResourceCategoryService.getById(id);
        if (category == null) {
            return R.fail("分类不存在");
        }
        return R.ok(category);
    }

    @GetMapping("/uid/{typeUid}")
    public R<PortalResourceCategory> getByTypeUid(@PathVariable Long typeUid) {
        PortalResourceCategory category = portalResourceCategoryService.getByTypeUid(typeUid);
        if (category == null) {
            return R.fail("分类不存在");
        }
        return R.ok(category);
    }

    @GetMapping("/parent/{parentId}")
    public R<List<PortalResourceCategory>> listByParentId(@PathVariable Long parentId) {
        List<PortalResourceCategory> list = portalResourceCategoryService.listByParentId(parentId);
        return R.ok(list);
    }

    @GetMapping("/search")
    public R<List<PortalResourceCategory>> searchByName(@RequestParam String typeName) {
        List<PortalResourceCategory> list = portalResourceCategoryService.listByTypeName(typeName);
        return R.ok(list);
    }

    @PostMapping
    public R<Boolean> save(@RequestBody PortalResourceCategory entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        boolean result = portalResourceCategoryService.save(entity);
        return R.ok(result);
    }

    @PostMapping("/batch")
    public R<Boolean> saveBatch(@RequestBody List<PortalResourceCategory> entities) {
        entities.forEach(e -> {
            e.setCreateTime(LocalDateTime.now());
            e.setUpdateTime(LocalDateTime.now());
        });
        boolean result = portalResourceCategoryService.saveBatch(entities);
        return R.ok(result);
    }

    @PutMapping
    public R<Boolean> update(@RequestBody PortalResourceCategory entity) {
        entity.setUpdateTime(LocalDateTime.now());
        boolean result = portalResourceCategoryService.updateById(entity);
        return R.ok(result);
    }

    @DeleteMapping("/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        boolean result = portalResourceCategoryService.removeById(id);
        return R.ok(result);
    }

    @DeleteMapping("/batch")
    public R<Boolean> removeBatch(@RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("ids");
        if (ids == null || ids.isEmpty()) {
            return R.fail("ids不能为空");
        }
        boolean result = portalResourceCategoryService.removeByIds(ids);
        return R.ok(result);
    }
}

package org.example.zygl.controller;

import lombok.RequiredArgsConstructor;
import org.example.zygl.entity.ResourceScopeSchool;
import org.example.zygl.service.ResourceScopeSchoolService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resource-scope-school")
@RequiredArgsConstructor
public class ResourceScopeSchoolController {

    private final ResourceScopeSchoolService resourceScopeSchoolService;

    @GetMapping("/list")
    public R<List<ResourceScopeSchool>> list() {
        return R.ok(resourceScopeSchoolService.list());
    }

    @GetMapping("/{id}")
    public R<ResourceScopeSchool> getById(@PathVariable Long id) {
        ResourceScopeSchool entity = resourceScopeSchoolService.getById(id);
        if (entity == null) {
            return R.fail("记录不存在");
        }
        return R.ok(entity);
    }

    @GetMapping("/resource/{resourceId}")
    public R<List<ResourceScopeSchool>> listByResourceId(@PathVariable Long resourceId) {
        return R.ok(resourceScopeSchoolService.listByResourceId(resourceId));
    }

    @GetMapping("/school/{schoolUid}")
    public R<List<ResourceScopeSchool>> listBySchoolUid(@PathVariable Long schoolUid) {
        return R.ok(resourceScopeSchoolService.listBySchoolUid(schoolUid));
    }

    @PostMapping
    public R<Boolean> save(@RequestBody ResourceScopeSchool entity) {
        boolean result = resourceScopeSchoolService.save(entity);
        return R.ok(result);
    }

    @PostMapping("/batch")
    public R<Boolean> saveBatch(@RequestBody List<ResourceScopeSchool> entities) {
        boolean result = resourceScopeSchoolService.saveBatch(entities);
        return R.ok(result);
    }

    @PutMapping
    public R<Boolean> update(@RequestBody ResourceScopeSchool entity) {
        boolean result = resourceScopeSchoolService.updateById(entity);
        return R.ok(result);
    }

    @DeleteMapping("/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        boolean result = resourceScopeSchoolService.removeById(id);
        return R.ok(result);
    }

    @DeleteMapping("/batch")
    public R<Boolean> removeBatch(@RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("ids");
        if (ids == null || ids.isEmpty()) {
            return R.fail("ids不能为空");
        }
        boolean result = resourceScopeSchoolService.removeByIds(ids);
        return R.ok(result);
    }
}

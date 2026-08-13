package org.example.zygl.controller;

import lombok.RequiredArgsConstructor;
import org.example.zygl.dto.BatchIdsRequest;
import org.example.zygl.entity.CloudStorage;
import org.example.zygl.service.CloudStorageService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cloud-storage")
@RequiredArgsConstructor
public class CloudStorageController {

    private final CloudStorageService cloudStorageService;

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

    @PostMapping
    public R<Boolean> save(@RequestBody CloudStorage entity) {
        boolean result = cloudStorageService.save(entity);
        return R.ok(result);
    }

    @PostMapping("/batch")
    public R<Boolean> saveBatch(@RequestBody List<CloudStorage> entities) {
        boolean result = cloudStorageService.saveBatch(entities);
        return R.ok(result);
    }

    @PutMapping
    public R<Boolean> update(@RequestBody CloudStorage entity) {
        boolean result = cloudStorageService.updateById(entity);
        return R.ok(result);
    }

    @DeleteMapping("/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        boolean result = cloudStorageService.removeById(id);
        return R.ok(result);
    }

    @DeleteMapping("/batch")
    public R<Boolean> removeBatch(@Valid @RequestBody BatchIdsRequest request) {
        boolean result = cloudStorageService.removeByIds(request.getIds());
        return R.ok(result);
    }
}

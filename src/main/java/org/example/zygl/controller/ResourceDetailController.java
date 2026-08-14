package org.example.zygl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.zygl.dto.BatchIdsRequest;
import org.example.zygl.entity.ResourceDetail;
import org.example.zygl.service.ResourceDetailService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/resource-detail")
@RequiredArgsConstructor
public class ResourceDetailController {

    private final ResourceDetailService resourceDetailService;

    @GetMapping("/page")
    public R<IPage<ResourceDetail>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam Long resourceId) {
        IPage<ResourceDetail> result = resourceDetailService.pageByResourceId(current, size, resourceId);
        return R.ok(result);
    }

    @GetMapping("/list")
    public R<List<ResourceDetail>> list() {
        return R.ok(resourceDetailService.list());
    }

    @GetMapping("/{id}")
    public R<ResourceDetail> getById(@PathVariable Long id) {
        ResourceDetail entity = resourceDetailService.getById(id);
        if (entity == null) {
            return R.fail("资源明细不存在");
        }
        return R.ok(entity);
    }

    @GetMapping("/resource/{resourceId}")
    public R<List<ResourceDetail>> listByResourceId(@PathVariable Long resourceId) {
        return R.ok(resourceDetailService.listByResourceId(resourceId));
    }

    @PostMapping
    public R<Boolean> save(@Valid @RequestBody ResourceDetail entity) {
        boolean result = resourceDetailService.save(entity);
        return R.ok(result);
    }

    @PostMapping("/batch")
    public R<Boolean> saveBatch(@RequestBody List<ResourceDetail> entities) {
        boolean result = resourceDetailService.saveBatch(entities);
        return R.ok(result);
    }

    @PutMapping
    public R<Boolean> update(@Valid @RequestBody ResourceDetail entity) {
        boolean result = resourceDetailService.updateById(entity);
        return R.ok(result);
    }

    @DeleteMapping("/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        boolean result = resourceDetailService.removeById(id);
        return R.ok(result);
    }

    @DeleteMapping("/batch")
    public R<Boolean> removeBatch(@Valid @RequestBody BatchIdsRequest request) {
        boolean result = resourceDetailService.removeByIds(request.getIds());
        return R.ok(result);
    }
}

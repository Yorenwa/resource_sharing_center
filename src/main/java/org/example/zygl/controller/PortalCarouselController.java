package org.example.zygl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.zygl.entity.PortalCarousel;
import org.example.zygl.service.PortalCarouselService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @PostMapping
    public R<Boolean> save(@RequestBody PortalCarousel entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        boolean result = portalCarouselService.save(entity);
        return R.ok(result);
    }

    @PostMapping("/batch")
    public R<Boolean> saveBatch(@RequestBody List<PortalCarousel> entities) {
        entities.forEach(e -> {
            e.setCreateTime(LocalDateTime.now());
            e.setUpdateTime(LocalDateTime.now());
        });
        boolean result = portalCarouselService.saveBatch(entities);
        return R.ok(result);
    }

    @PutMapping
    public R<Boolean> update(@RequestBody PortalCarousel entity) {
        entity.setUpdateTime(LocalDateTime.now());
        boolean result = portalCarouselService.updateById(entity);
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

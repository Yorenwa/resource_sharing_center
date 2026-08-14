package org.example.zygl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.zygl.dto.BatchIdsRequest;
import org.example.zygl.entity.PortalResourceCategory;
import org.example.zygl.entity.PortalResourceCategoryNode;
import org.example.zygl.service.PortalResourceCategoryService;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 门户资源分类管理接口
 * <p>
 * 业务特性：
 * <ul>
 *   <li>分类支持两级层级结构（一级分类 → 二级分类），level 和 path 由系统自动计算</li>
 *   <li>删除分类时校验是否存在子分类，存在则拒绝删除</li>
 *   <li>支持树形查询接口，返回层级结构供前端展示</li>
 * </ul>
 */
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

    /**
     * 查询分类树形结构
     * <p>
     * 返回所有分类的父子层级结构，供前端以树形表格形式展示。
     * 截图中的"门户资源分类列表"即使用此接口数据。
     *
     * @param type   资源类型过滤（可选）
     * @param status 状态过滤（可选）
     * @return 树形结构列表
     */
    @GetMapping("/tree")
    public R<List<PortalResourceCategoryNode>> tree(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status) {
        List<PortalResourceCategoryNode> result = portalResourceCategoryService.tree(type, status);
        return R.ok(result);
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

    /**
     * 新增分类（一级或二级）
     * <p>
     * 系统根据 parentId 自动计算 level 和 path：
     * parentId=0 或不传 → 一级分类（level=1，path=null）
     * parentId=父分类ID → 二级分类（level=父level+1，path=父path/父pkId）
     *
     * @param entity 分类实体（parentId 为 0 或 null 表示一级分类）
     * @return 操作结果
     */
    @PostMapping
    public R<Boolean> save(@Valid @RequestBody PortalResourceCategory entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        boolean result = portalResourceCategoryService.save(entity);
        return R.ok(result);
    }

    /**
     * 新增子分类
     * <p>
     * 明确语义的快捷接口，自动根据父分类计算 level 和 path。
     * 对应截图中的"新增子分类"按钮。
     *
     * @param parentId 父分类ID
     * @param entity   子分类实体（不需填写 parentId/level/path，由系统自动计算）
     * @return 操作结果
     */
    @PostMapping("/child/{parentId}")
    public R<Boolean> addChild(@PathVariable Long parentId,
                                @Valid @RequestBody PortalResourceCategory entity) {
        try {
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            boolean result = portalResourceCategoryService.addChild(parentId, entity);
            return R.ok(result);
        } catch (RuntimeException e) {
            return R.fail(e.getMessage());
        }
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
    public R<Boolean> update(@Valid @RequestBody PortalResourceCategory entity) {
        entity.setUpdateTime(LocalDateTime.now());
        boolean result = portalResourceCategoryService.updateById(entity);
        return R.ok(result);
    }

    /**
     * 设置分类状态（启用/停用）
     *
     * @param id     分类主键ID
     * @param status 目标状态：1-启用，0-停用
     * @return 操作结果
     */
    @PutMapping("/status/{id}")
    public R<Boolean> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean result = portalResourceCategoryService.updateStatus(id, status);
        return R.ok(result);
    }

    /**
     * 切换分类状态（启用↔停用一键切换）
     * <p>
     * 对应截图中的"启用/停用"状态标签点击切换。
     *
     * @param id 分类主键ID
     * @return 操作结果
     */
    @PutMapping("/toggle/{id}")
    public R<Boolean> toggleStatus(@PathVariable Long id) {
        boolean result = portalResourceCategoryService.toggleStatus(id);
        return R.ok(result);
    }

    /**
     * 删除分类（带子分类校验）
     * <p>
     * 若存在子分类则拒绝删除，对应截图中的删除操作。
     *
     * @param id 分类主键ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        try {
            boolean result = portalResourceCategoryService.removeWithCheck(id);
            return R.ok(result);
        } catch (RuntimeException e) {
            return R.fail(e.getMessage());
        }
    }

    @DeleteMapping("/batch")
    public R<Boolean> removeBatch(@Valid @RequestBody BatchIdsRequest request) {
        boolean result = portalResourceCategoryService.removeByIds(request.getIds());
        return R.ok(result);
    }
}

package org.example.zygl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.zygl.entity.PortalResourceCategory;
import org.example.zygl.entity.PortalResourceCategoryNode;

import java.util.List;

public interface PortalResourceCategoryService extends IService<PortalResourceCategory> {

    List<PortalResourceCategory> listByType(Integer type);

    List<PortalResourceCategory> listByParentId(Long parentId);

    PortalResourceCategory getByTypeUid(Long typeUid);

    IPage<PortalResourceCategory> pageByCondition(Integer current, Integer size, Integer type, String typeName, Integer status);

    List<PortalResourceCategory> listByTypeName(String typeName);

    /**
     * 查询分类树形结构
     * <p>
     * 返回所有分类并组装为父子层级结构，供前端以树形表格形式展示。
     *
     * @param type   资源类型过滤（可选）
     * @param status 状态过滤（可选）
     * @return 树形结构列表
     */
    List<PortalResourceCategoryNode> tree(Integer type, Integer status);

    /**
     * 新增子分类
     * <p>
     * 自动根据父分类计算 level 和 path。
     *
     * @param parentId 父分类ID
     * @param entity   子分类实体（不需填写 level/path，由系统自动计算）
     * @return 是否保存成功
     */
    boolean addChild(Long parentId, PortalResourceCategory entity);

    /**
     * 更新分类状态（启用/停用）
     *
     * @param id     分类主键ID
     * @param status 目标状态：1-启用，0-停用
     * @return 是否更新成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 切换分类状态（启用↔停用）
     *
     * @param id 分类主键ID
     * @return 是否切换成功
     */
    boolean toggleStatus(Long id);

    /**
     * 删除分类
     * <p>
     * 若存在子分类则拒绝删除，需先删除所有子分类。
     *
     * @param id 分类主键ID
     * @return 是否删除成功
     * @throws RuntimeException 存在子分类时抛出
     */
    boolean removeWithCheck(Long id);
}

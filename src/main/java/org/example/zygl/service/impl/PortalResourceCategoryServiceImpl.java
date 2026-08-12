package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.entity.PortalResourceCategory;
import org.example.zygl.entity.PortalResourceCategoryNode;
import org.example.zygl.mapper.PortalResourceCategoryMapper;
import org.example.zygl.service.PortalResourceCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 门户资源分类服务实现类
 * <p>
 * 核心业务规则：
 * <ul>
 *   <li>path 字段存储 pkId 路径（如 "/1/2"），确保唯一性和稳定性，分类改名不影响 path</li>
 *   <li>查询时动态计算 namePath（名称路径，如 "区级安全宣传/政策法规"）供前端展示</li>
 *   <li>一级分类：level=1，path=null，parentId=0</li>
 *   <li>二级分类：level=父级level+1，path="/父级pkId"，parentId=父级pkId</li>
 *   <li>删除分类前需校验是否存在子分类，存在则拒绝删除</li>
 * </ul>
 */
@Service
public class PortalResourceCategoryServiceImpl
        extends ServiceImpl<PortalResourceCategoryMapper, PortalResourceCategory>
        implements PortalResourceCategoryService {

    @Override
    public List<PortalResourceCategory> listByType(Integer type) {
        return baseMapper.selectByType(type);
    }

    @Override
    public List<PortalResourceCategory> listByParentId(Long parentId) {
        return baseMapper.selectByParentId(parentId);
    }

    @Override
    public PortalResourceCategory getByTypeUid(Long typeUid) {
        return baseMapper.selectByTypeUid(typeUid);
    }

    @Override
    public IPage<PortalResourceCategory> pageByCondition(Integer current, Integer size, Integer type, String typeName, Integer status) {
        Page<PortalResourceCategory> page = new Page<>(current, size);
        return baseMapper.selectPageByCondition(page, type, typeName, status);
    }

    @Override
    public List<PortalResourceCategory> listByTypeName(String typeName) {
        return baseMapper.selectByTypeName(typeName);
    }

    /**
     * 组装分类树形结构
     * <p>
     * 从数据库加载所有符合条件的分类，按 parentId 组装为父子层级结构。
     * 同时递归计算 namePath（名称路径），供前端直接展示。
     *
     * @param type   资源类型过滤（可选）
     * @param status 状态过滤（可选）
     * @return 树形结构列表
     */
    @Override
    public List<PortalResourceCategoryNode> tree(Integer type, Integer status) {
        List<PortalResourceCategory> all = baseMapper.selectAll(type, status);

        // 按 parentId 分组
        Map<Long, List<PortalResourceCategory>> parentMap = all.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getParentId() != null ? c.getParentId() : 0L));

        // 转换并组装树形结构，一级分类 namePath = typeName
        List<PortalResourceCategoryNode> roots = new ArrayList<>();
        for (PortalResourceCategory cat : all) {
            if (cat.getParentId() == null || cat.getParentId() == 0L) {
                roots.add(toNode(cat, parentMap, cat.getTypeName()));
            }
        }
        return roots;
    }

    /**
     * 递归构建树节点，同时计算 namePath
     *
     * @param cat        当前分类
     * @param parentMap  按 parentId 分组的全量分类
     * @param parentNamePath 父级的名称路径（一级分类传入自身 typeName）
     */
    private PortalResourceCategoryNode toNode(PortalResourceCategory cat,
                                               Map<Long, List<PortalResourceCategory>> parentMap,
                                               String parentNamePath) {
        PortalResourceCategoryNode node = new PortalResourceCategoryNode();
        node.setPkId(cat.getPkId());
        node.setTypeUid(cat.getTypeUid());
        node.setTypeName(cat.getTypeName());
        node.setParentId(cat.getParentId());
        node.setLevel(cat.getLevel());
        node.setPath(cat.getPath());
        node.setNamePath(parentNamePath);
        node.setType(cat.getType());
        node.setOrder(cat.getOrder());
        node.setStatus(cat.getStatus());
        node.setCreator(cat.getCreator());
        node.setUpdater(cat.getUpdater());
        node.setTenantId(cat.getTenantId());

        List<PortalResourceCategory> children = parentMap.get(cat.getPkId());
        if (children != null && !children.isEmpty()) {
            List<PortalResourceCategoryNode> childNodes = new ArrayList<>();
            for (PortalResourceCategory child : children) {
                // 子分类 namePath = 父namePath + "/" + 子分类名称
                String childNamePath = parentNamePath + "/" + child.getTypeName();
                childNodes.add(toNode(child, parentMap, childNamePath));
            }
            node.setChildren(childNodes);
        }
        return node;
    }

    /**
     * 新增子分类，自动计算 level 和 path
     * <p>
     * path 使用 pkId 拼接，确保唯一性和稳定性：
     * <ul>
     *   <li>level = 父级level + 1</li>
     *   <li>path = 父级path + "/" + 父级pkId</li>
     * </ul>
     *
     * @param parentId 父分类ID
     * @param entity   子分类实体（level/path 由系统覆盖）
     * @return 是否保存成功
     * @throws RuntimeException 父分类不存在时抛出
     */
    @Override
    @Transactional
    public boolean addChild(Long parentId, PortalResourceCategory entity) {
        PortalResourceCategory parent = getById(parentId);
        if (parent == null) {
            throw new RuntimeException("父分类不存在");
        }
        entity.setParentId(parentId);
        entity.setLevel(parent.getLevel() + 1);
        entity.setPath(buildPath(parent.getPath(), parentId));
        return super.save(entity);
    }

    /**
     * 构建 pkId 路径
     * <p>
     * 规则：
     * <ul>
     *   <li>一级分类：path = null</li>
     *   <li>二级分类：path = "/父pkId"</li>
     * </ul>
     *
     * @param parentPath 父分类的 path（一级分类的父 path 为 null）
     * @param parentId   父分类的 pkId
     * @return pkId 路径
     */
    private String buildPath(String parentPath, Long parentId) {
        if (parentPath == null || parentPath.isEmpty()) {
            return "/" + parentId;
        }
        return parentPath + "/" + parentId;
    }

    /**
     * 更新分类状态（启用/停用）
     */
    @Override
    @Transactional
    public boolean updateStatus(Long id, Integer status) {
        return baseMapper.updateStatus(id, status) > 0;
    }

    /**
     * 切换分类状态（启用↔停用）
     */
    @Override
    @Transactional
    public boolean toggleStatus(Long id) {
        PortalResourceCategory entity = getById(id);
        if (entity == null) {
            return false;
        }
        Integer newStatus = (entity.getStatus() == 1) ? 0 : 1;
        return baseMapper.updateStatus(id, newStatus) > 0;
    }

    /**
     * 删除分类（带子分类校验）
     * <p>
     * 若存在子分类则拒绝删除，需先删除所有子分类后才能删除当前分类。
     *
     * @param id 分类主键ID
     * @return 是否删除成功
     * @throws RuntimeException 存在子分类时抛出
     */
    @Override
    @Transactional
    public boolean removeWithCheck(Long id) {
        int childrenCount = baseMapper.countChildren(id);
        if (childrenCount > 0) {
            throw new RuntimeException("该分类下存在" + childrenCount + "个子分类，无法删除");
        }
        return super.removeById(id);
    }

    /**
     * 重写 save 方法，自动计算 level 和 path
     * <p>
     * path 使用 pkId 拼接，改名时无需级联更新子孙：
     * <ul>
     *   <li>parentId=0 或 null → 一级分类，level=1，path=null</li>
     *   <li>parentId=有效值 → 二级分类，level=父level+1，path=父path/父pkId</li>
     * </ul>
     */
    @Override
    public boolean save(PortalResourceCategory entity) {
        if (entity.getParentId() == null || entity.getParentId() == 0L) {
            entity.setLevel(1);
            entity.setPath(null);
        } else {
            PortalResourceCategory parent = getById(entity.getParentId());
            if (parent != null) {
                entity.setLevel(parent.getLevel() + 1);
                entity.setPath(buildPath(parent.getPath(), parent.getPkId()));
            } else {
                entity.setLevel(1);
                entity.setPath(null);
            }
        }
        return super.save(entity);
    }
}

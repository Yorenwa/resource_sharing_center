package org.example.zygl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.zygl.entity.PortalResourceCategory;
import org.example.zygl.service.PortalResourceCategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PortalResourceCategoryLevelPathTest {

    @Autowired
    private PortalResourceCategoryService portalResourceCategoryService;

    private PortalResourceCategory buildCategory(String typeName, Long parentId, Integer level,
                                                  String path, Integer type, Double order) {
        PortalResourceCategory entity = new PortalResourceCategory();
        entity.setTypeUid(null);
        entity.setTypeName(typeName);
        entity.setParentId(parentId);
        entity.setLevel(level);
        entity.setPath(path);
        entity.setType(type);
        entity.setOrder(order);
        entity.setStatus(1);
        entity.setCreator("test_user");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdater("test_user");
        entity.setUpdateTime(LocalDateTime.now());
        entity.setTenantId("tenant_test");
        return entity;
    }

    @Test
    @DisplayName("保存一级分类 - 验证 level=1, path=null")
    void testSaveLevel1Category() {
        PortalResourceCategory category = buildCategory("测试一级分类", 0L, 1, null, 1, 99.0);

        boolean saved = portalResourceCategoryService.save(category);
        assertTrue(saved, "保存应成功");
        assertNotNull(category.getPkId(), "保存后应生成主键");

        // 查询验证
        PortalResourceCategory queried = portalResourceCategoryService.getById(category.getPkId());
        assertNotNull(queried, "查询结果不应为空");
        assertEquals(1, queried.getLevel(), "level 应为 1");
        assertNull(queried.getPath(), "一级分类 path 应为 null");
        assertEquals("测试一级分类", queried.getTypeName());
        System.out.println("[一级分类保存验证] 通过 -> pkId=" + queried.getPkId()
                + ", level=" + queried.getLevel() + ", path=" + queried.getPath());
    }

    @Test
    @DisplayName("保存二级分类 - 验证 level=2, path=父级标识")
    void testSaveLevel2Category() {
        // 先保存一级分类
        PortalResourceCategory parent = buildCategory("测试父分类", 0L, 1, null, 1, 98.0);
        portalResourceCategoryService.save(parent);
        assertNotNull(parent.getPkId(), "父分类保存后应生成主键");

        // 保存二级分类，path 指向父级
        PortalResourceCategory child = buildCategory("测试子分类", parent.getPkId(), 2,
                String.valueOf(parent.getPkId()), 1, 97.0);
        boolean saved = portalResourceCategoryService.save(child);
        assertTrue(saved, "保存应成功");

        // 查询验证
        PortalResourceCategory queried = portalResourceCategoryService.getById(child.getPkId());
        assertNotNull(queried, "查询结果不应为空");
        assertEquals(2, queried.getLevel(), "level 应为 2");
        assertEquals(String.valueOf(parent.getPkId()), queried.getPath(), "path 应为父级主键");
        assertEquals(parent.getPkId(), queried.getParentId(), "parentId 应为父级主键");
        System.out.println("[二级分类保存验证] 通过 -> pkId=" + queried.getPkId()
                + ", parentId=" + queried.getParentId()
                + ", level=" + queried.getLevel() + ", path=" + queried.getPath());
    }

    @Test
    @DisplayName("更新 level 和 path 字段")
    void testUpdateLevelAndPath() {
        // 保存初始数据
        PortalResourceCategory category = buildCategory("待更新分类", 0L, 1, null, 1, 96.0);
        portalResourceCategoryService.save(category);

        // 更新 level 和 path
        category.setLevel(2);
        category.setPath("updated_path_value");
        category.setUpdateTime(LocalDateTime.now());
        boolean updated = portalResourceCategoryService.updateById(category);
        assertTrue(updated, "更新应成功");

        // 查询验证
        PortalResourceCategory queried = portalResourceCategoryService.getById(category.getPkId());
        assertNotNull(queried, "查询结果不应为空");
        assertEquals(2, queried.getLevel(), "更新后 level 应为 2");
        assertEquals("updated_path_value", queried.getPath(), "更新后 path 应为 updated_path_value");
        System.out.println("[更新验证] 通过 -> level=" + queried.getLevel()
                + ", path=" + queried.getPath());
    }

    @Test
    @DisplayName("分页查询 - 验证 level 和 path 正确返回")
    void testPageQueryWithLevelAndPath() {
        // 保存多条测试数据
        PortalResourceCategory cat1 = buildCategory("分页测试分类1", 0L, 1, null, 1, 95.0);
        PortalResourceCategory cat2 = buildCategory("分页测试分类2", 0L, 1, null, 2, 94.0);
        PortalResourceCategory cat3 = buildCategory("分页测试子分类1", 1L, 2, "1", 1, 93.0);
        portalResourceCategoryService.saveBatch(Arrays.asList(cat1, cat2, cat3));

        // 分页查询（按 type=1 过滤）
        IPage<PortalResourceCategory> page = portalResourceCategoryService.pageByCondition(
                1, 100, 1, "分页测试", null);

        assertNotNull(page, "分页结果不应为空");
        assertTrue(page.getRecords().size() >= 2, "应至少返回2条记录");

        // 验证每条记录的 level 和 path 都有值
        for (PortalResourceCategory record : page.getRecords()) {
            assertNotNull(record.getLevel(), "level 不应为 null");
            System.out.println("[分页查询验证] -> typeName=" + record.getTypeName()
                    + ", level=" + record.getLevel() + ", path=" + record.getPath());
        }
    }

    @Test
    @DisplayName("按 parentId 查询子分类 - 验证 level 和 path")
    void testListByParentId() {
        // 保存父分类
        PortalResourceCategory parent = buildCategory("父子查询父分类", 0L, 1, null, 1, 92.0);
        portalResourceCategoryService.save(parent);

        // 保存子分类
        PortalResourceCategory child1 = buildCategory("子分类A", parent.getPkId(), 2,
                String.valueOf(parent.getPkId()), 1, 91.0);
        PortalResourceCategory child2 = buildCategory("子分类B", parent.getPkId(), 2,
                String.valueOf(parent.getPkId()), 1, 90.0);
        portalResourceCategoryService.saveBatch(Arrays.asList(child1, child2));

        // 按 parentId 查询
        List<PortalResourceCategory> children = portalResourceCategoryService.listByParentId(parent.getPkId());
        assertNotNull(children, "查询结果不应为空");
        assertFalse(children.isEmpty(), "应至少返回1条子分类");

        for (PortalResourceCategory child : children) {
            if (child.getTypeName().startsWith("子分类")) {
                assertEquals(2, child.getLevel(), "子分类 level 应为 2");
                assertEquals(String.valueOf(parent.getPkId()), child.getPath(), "子分类 path 应为父级主键");
                System.out.println("[按parentId查询验证] -> typeName=" + child.getTypeName()
                        + ", level=" + child.getLevel() + ", path=" + child.getPath());
            }
        }
    }

    @Test
    @DisplayName("批量保存 - 验证 level 和 path 批量写入")
    void testBatchSaveWithLevelAndPath() {
        PortalResourceCategory cat1 = buildCategory("批量分类1", 0L, 1, null, 1, 89.0);
        PortalResourceCategory cat2 = buildCategory("批量分类2", 0L, 1, null, 2, 88.0);

        boolean saved = portalResourceCategoryService.saveBatch(Arrays.asList(cat1, cat2));
        assertTrue(saved, "批量保存应成功");
        assertNotNull(cat1.getPkId(), "cat1 应有主键");
        assertNotNull(cat2.getPkId(), "cat2 应有主键");

        // 逐条查询验证
        PortalResourceCategory q1 = portalResourceCategoryService.getById(cat1.getPkId());
        PortalResourceCategory q2 = portalResourceCategoryService.getById(cat2.getPkId());

        assertEquals(1, q1.getLevel(), "cat1 level 应为 1");
        assertNull(q1.getPath(), "cat1 path 应为 null");
        assertEquals(1, q2.getLevel(), "cat2 level 应为 1");
        assertNull(q2.getPath(), "cat2 path 应为 null");

        System.out.println("[批量保存验证] 通过 -> cat1.level=" + q1.getLevel()
                + ", cat2.level=" + q2.getLevel());
    }
}

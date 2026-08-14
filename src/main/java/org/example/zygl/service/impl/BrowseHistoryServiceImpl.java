package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.entity.BrowseHistory;
import org.example.zygl.entity.PortalResourceCategory;
import org.example.zygl.mapper.BrowseHistoryMapper;
import org.example.zygl.mapper.PortalResourceCategoryMapper;
import org.example.zygl.service.BrowseHistoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 浏览记录服务实现类
 * <p>
 * 核心业务：
 * <ul>
 *   <li>分页查询支持多条件过滤（用户、资源类型、分类路径、资源名称）</li>
 *   <li>查询完成后自动填充 categoryNamePath（分类名称路径），供前端展示</li>
 * </ul>
 */
@Service
public class BrowseHistoryServiceImpl
        extends ServiceImpl<BrowseHistoryMapper, BrowseHistory>
        implements BrowseHistoryService {

    private final PortalResourceCategoryMapper categoryMapper;

    public BrowseHistoryServiceImpl(PortalResourceCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public IPage<BrowseHistory> pageByCondition(Integer current, Integer size, Long userId,
                                                Integer resourceType, String categoryPath,
                                                String resourceName) {
        Page<BrowseHistory> page = new Page<>(current, size);
        IPage<BrowseHistory> result = baseMapper.selectPageByCondition(page, userId,
                resourceType, categoryPath, resourceName);

        // 填充分类名称路径
        fillCategoryNamePath(result.getRecords());

        return result;
    }

    @Override
    public List<BrowseHistory> listByUserId(Long userId) {
        List<BrowseHistory> list = baseMapper.selectByUserId(userId);
        fillCategoryNamePath(list);
        return list;
    }

    @Override
    public List<BrowseHistory> listByResourceId(Long resourceId) {
        List<BrowseHistory> list = baseMapper.selectByResourceId(resourceId);
        fillCategoryNamePath(list);
        return list;
    }

    /**
     * 填充分类名称路径
     * <p>
     * 遍历浏览记录列表，根据 categoryPath（pkId 路径，如 "/1/2"）解析出各层级分类，
     * 拼接为名称路径（如 "区级安全宣传/安全制度"）。
     * 采用批量查询避免 N+1 性能问题。
     *
     * @param list 浏览记录列表
     */
    private void fillCategoryNamePath(List<BrowseHistory> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        // 1. 收集所有 categoryPath 中涉及的 pkId
        List<Long> allPks = new ArrayList<>();
        for (BrowseHistory h : list) {
            if (h.getCategoryPath() != null && !h.getCategoryPath().isEmpty()) {
                String[] parts = h.getCategoryPath().split("/");
                for (String part : parts) {
                    if (!part.isEmpty()) {
                        try {
                            allPks.add(Long.parseLong(part));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        }

        if (allPks.isEmpty()) {
            return;
        }

        // 2. 批量查询分类，构建 pkId → 实体映射
        List<PortalResourceCategory> categories = categoryMapper.selectBatchIds(allPks);
        Map<Long, PortalResourceCategory> categoryMap = categories.stream()
                .collect(Collectors.toMap(PortalResourceCategory::getPkId, c -> c, (a, b) -> a));

        // 3. 为每条记录构建名称路径
        for (BrowseHistory h : list) {
            if (h.getCategoryPath() == null || h.getCategoryPath().isEmpty()) {
                continue;
            }
            String[] parts = h.getCategoryPath().split("/");
            StringBuilder namePath = new StringBuilder();
            for (String part : parts) {
                if (part.isEmpty()) {
                    continue;
                }
                try {
                    Long pkId = Long.parseLong(part);
                    PortalResourceCategory cat = categoryMap.get(pkId);
                    if (cat != null) {
                        if (namePath.length() > 0) {
                            namePath.append("/");
                        }
                        namePath.append(cat.getTypeName());
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            h.setCategoryNamePath(namePath.toString());
        }
    }
}

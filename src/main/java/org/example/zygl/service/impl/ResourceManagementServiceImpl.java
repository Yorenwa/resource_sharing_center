package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.converter.ResourceUploadConverter;
import org.example.zygl.dto.CategoryStatDTO;
import org.example.zygl.dto.ResourceUploadRequest;
import org.example.zygl.entity.PortalResourceCategory;
import org.example.zygl.entity.ResourceDetail;
import org.example.zygl.entity.ResourceManagement;
import org.example.zygl.enums.ResourceScopeEnum;
import org.example.zygl.enums.ResourceStatusEnum;
import org.example.zygl.enums.ToggleStatusEnum;
import org.example.zygl.mapper.PortalResourceCategoryMapper;
import org.example.zygl.mapper.ResourceManagementMapper;
import org.example.zygl.service.ResourceDetailService;
import org.example.zygl.service.ResourceManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源管理服务实现类
 * <p>
 * 核心业务规则：
 * <ul>
 *   <li>单个一级分类下最多 4 个置顶资源，超上限拒绝置顶</li>
 *   <li>审核状态：1-待审核，2-已通过，3-已驳回</li>
 *   <li>category_path 存储 pkId 路径，用于按分类层级过滤资源</li>
 * </ul>
 */
@Service
public class ResourceManagementServiceImpl
        extends ServiceImpl<ResourceManagementMapper, ResourceManagement>
        implements ResourceManagementService {

    /**
     * 单个一级分类最大置顶数
     */
    public static final int MAX_PINNED_PER_CATEGORY = 4;

    private final PortalResourceCategoryMapper categoryMapper;
    private final ResourceDetailService resourceDetailService;
    private final ResourceUploadConverter resourceUploadConverter;

    public ResourceManagementServiceImpl(PortalResourceCategoryMapper categoryMapper,
                                          ResourceDetailService resourceDetailService,
                                          ResourceUploadConverter resourceUploadConverter) {
        this.categoryMapper = categoryMapper;
        this.resourceDetailService = resourceDetailService;
        this.resourceUploadConverter = resourceUploadConverter;
    }

    @Override
    public IPage<ResourceManagement> pageByCondition(Integer current, Integer size, String resourceName,
                                                     Integer resourceType, Long categoryId,
                                                     Long level1CategoryId, Long level2CategoryId,
                                                     Integer status, Integer scope,
                                                     Integer recommended, Integer pinned,
                                                     String uploader, Long schoolUid) {
        Page<ResourceManagement> page = new Page<>(current, size);
        return baseMapper.selectPageByCondition(page, resourceName, resourceType, categoryId,
                level1CategoryId, level2CategoryId, status, scope, recommended, pinned,
                uploader, schoolUid);
    }

    @Override
    public List<ResourceManagement> listByCategoryId(Long categoryId) {
        return baseMapper.selectByCategoryId(categoryId);
    }

    @Override
    public List<ResourceManagement> listByUploader(String uploader) {
        return baseMapper.selectByUploader(uploader);
    }

    @Override
    public List<ResourceManagement> listRecommended() {
        return baseMapper.selectRecommended();
    }

    @Override
    public List<ResourceManagement> listPinned() {
        return baseMapper.selectPinned();
    }

    /**
     * 资源点击量原子递增
     */
    @Override
    public boolean addClick(Long id) {
        return baseMapper.addClick(id) > 0;
    }

    /**
     * 获取分类统计信息
     * <p>
     * 返回指定分类（含子分类）下的全部资源数、置顶数、推荐数。
     *
     * @param categoryPath 分类路径（可选）
     * @return 统计结果 DTO
     */
    @Override
    public CategoryStatDTO categoryStats(String categoryPath) {
        return baseMapper.categoryStats(categoryPath);
    }

    /**
     * 设置推荐状态
     */
    @Override
    @Transactional
    public boolean updateRecommended(Long id, Integer recommended) {
        return baseMapper.updateRecommended(id, recommended) > 0;
    }

    /**
     * 设置置顶状态（含置顶上限校验）
     * <p>
     * 当设置为置顶时，校验该资源所属一级分类下的置顶资源数是否已达上限（4个）。
     * 已置顶的资源再次置顶允许通过（幂等）。
     *
     * @param id     资源ID
     * @param pinned 1-置顶，0-取消置顶
     * @throws RuntimeException 超过上限时抛出
     */
    @Override
    @Transactional
    public boolean updatePinned(Long id, Integer pinned) {
        if (pinned != null && pinned == ToggleStatusEnum.ON.getCode()) {
            ResourceManagement entity = getById(id);
            if (entity == null) {
                throw new RuntimeException("资源不存在");
            }
            // 如果已是置顶状态，直接允许（幂等）
            if (entity.getPinned() != null && entity.getPinned() == ToggleStatusEnum.ON.getCode()) {
                return true;
            }
            // 校验一级分类下的置顶资源数
            Long level1Id = resolveLevel1CategoryId(entity.getCategoryId());
            int pinnedCount = baseMapper.countPinnedByCategoryId(level1Id);
            if (pinnedCount >= MAX_PINNED_PER_CATEGORY) {
                throw new RuntimeException("该分类下已置顶资源已达上限(" + MAX_PINNED_PER_CATEGORY + "个)");
            }
        }
        return baseMapper.updatePinned(id, pinned) > 0;
    }

    /**
     * 解析资源所属的一级分类ID
     * <p>
     * 如果资源属于二级分类，向上查找一级分类ID；如果本身就是一级分类则直接返回。
     */
    private Long resolveLevel1CategoryId(Long categoryId) {
        if (categoryId == null) {
            return 0L;
        }
        PortalResourceCategory category = categoryMapper.selectById(categoryId);
        if (category == null) {
            return categoryId;
        }
        if (category.getLevel() != null && category.getLevel() == PortalResourceCategoryServiceImpl.LEVEL_1) {
            return categoryId;
        }
        // 二级分类，返回其父分类ID
        if (category.getParentId() != null) {
            return category.getParentId();
        }
        return categoryId;
    }

    /**
     * 切换推荐状态
     */
    @Override
    @Transactional
    public boolean toggleRecommended(Long id) {
        ResourceManagement entity = getById(id);
        if (entity == null) {
            return false;
        }
        Integer newVal = (entity.getRecommended() != null && entity.getRecommended() == ToggleStatusEnum.ON.getCode())
                ? ToggleStatusEnum.OFF.getCode() : ToggleStatusEnum.ON.getCode();
        return updateRecommended(id, newVal);
    }

    /**
     * 切换置顶状态
     */
    @Override
    @Transactional
    public boolean togglePinned(Long id) {
        ResourceManagement entity = getById(id);
        if (entity == null) {
            return false;
        }
        Integer newVal = (entity.getPinned() != null && entity.getPinned() == ToggleStatusEnum.ON.getCode())
                ? ToggleStatusEnum.OFF.getCode() : ToggleStatusEnum.ON.getCode();
        return updatePinned(id, newVal);
    }

    /**
     * 批量设置推荐状态
     */
    @Override
    @Transactional
    public boolean batchUpdateRecommended(List<Long> ids, Integer recommended) {
        int rows = baseMapper.batchUpdateRecommended(ids, recommended);
        return rows > 0;
    }

    /**
     * 批量设置置顶状态（含置顶上限校验）
     * <p>
     * 逐条校验每个资源所属分类的置顶上限。
     *
     * @throws RuntimeException 超过上限时抛出
     */
    @Override
    @Transactional
    public boolean batchUpdatePinned(List<Long> ids, Integer pinned) {
        if (pinned != null && pinned == ToggleStatusEnum.ON.getCode()) {
            // 校验每条资源所属分类的置顶上限
            for (Long id : ids) {
                ResourceManagement entity = getById(id);
                if (entity != null && (entity.getPinned() == null
                        || !entity.getPinned().equals(ToggleStatusEnum.ON.getCode()))) {
                    Long level1Id = resolveLevel1CategoryId(entity.getCategoryId());
                    int pinnedCount = baseMapper.countPinnedByCategoryId(level1Id);
                    if (pinnedCount >= MAX_PINNED_PER_CATEGORY) {
                        throw new RuntimeException("分类[" + level1Id + "]下已置顶资源已达上限(" + MAX_PINNED_PER_CATEGORY + "个)");
                    }
                }
            }
        }
        int rows = baseMapper.batchUpdatePinned(ids, pinned);
        return rows > 0;
    }

    /**
     * 审核通过
     */
    @Override
    @Transactional
    public boolean approve(Long id) {
        return baseMapper.updateStatus(id, ResourceStatusEnum.APPROVED.getCode(), null) > 0;
    }

    /**
     * 审核驳回
     */
    @Override
    @Transactional
    public boolean reject(Long id, String rejectionReason) {
        return baseMapper.updateStatus(id, ResourceStatusEnum.REJECTED.getCode(), rejectionReason) > 0;
    }

    /**
     * 批量审核通过
     */
    @Override
    @Transactional
    public boolean batchApprove(List<Long> ids) {
        int rows = baseMapper.batchUpdateStatus(ids, ResourceStatusEnum.APPROVED.getCode(), null);
        return rows > 0;
    }

    /**
     * 批量审核驳回
     */
    @Override
    @Transactional
    public boolean batchReject(List<Long> ids, String rejectionReason) {
        int rows = baseMapper.batchUpdateStatus(ids, ResourceStatusEnum.REJECTED.getCode(), rejectionReason);
        return rows > 0;
    }

    /**
     * 保存资源及明细（上传流程）
     * <p>
     * 优化点：
     * <ul>
     *   <li>使用 MapStruct 自动映射 DTO → Entity，消除手动 set 样板代码</li>
     *   <li>使用枚举替代魔法数字，提升可读性</li>
     *   <li>使用 saveBatch 批量插入明细，将 N 次 SQL 降为 1 次</li>
     * </ul>
     * 全程事务保护，任一环节失败则回滚。
     *
     * @param request 上传请求体
     * @return 资源主表 ID
     */
    @Override
    @Transactional
    public Long saveWithDetails(ResourceUploadRequest request) {
        LocalDateTime now = LocalDateTime.now();

        // 1. DTO → Entity（MapStruct 自动映射公共字段）
        ResourceManagement entity = resourceUploadConverter.toEntity(request);

        // 2. 填充默认值（使用枚举，消除魔法数字）
        entity.setStatus(ResourceStatusEnum.PENDING.getCode());
        entity.setRecommended(ToggleStatusEnum.OFF.getCode());
        entity.setPinned(ToggleStatusEnum.OFF.getCode());
        entity.setClicks(0);
        if (entity.getScope() == null) {
            entity.setScope(ResourceScopeEnum.PUBLIC.getCode());
        }
        entity.setCreator(request.getUploader());
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        this.save(entity);

        Long resourceId = entity.getPkId();

        // 3. 批量保存资源明细（MapStruct 映射 + saveBatch 一次插入）
        List<ResourceDetail> details = resourceUploadConverter.toDetailEntities(request.getDetails());
        int episodeNo = 1;
        for (ResourceDetail detail : details) {
            detail.setResourceId(resourceId);
            if (detail.getEpisodeNo() == null || detail.getEpisodeNo() == 0) {
                detail.setEpisodeNo(episodeNo);
            }
            episodeNo++;
        }
        resourceDetailService.saveBatch(details);

        return resourceId;
    }
}

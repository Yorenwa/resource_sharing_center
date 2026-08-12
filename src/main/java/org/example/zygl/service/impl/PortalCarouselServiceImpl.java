package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.entity.PortalCarousel;
import org.example.zygl.mapper.PortalCarouselMapper;
import org.example.zygl.service.PortalCarouselService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 门户轮播图服务实现类
 * <p>
 * 业务规则：轮播图最多同时存在 {@link PortalCarouselService#MAX_TOTAL} 个，
 * 其中启用状态的最多 {@link PortalCarouselService#MAX_ENABLED} 个。
 * 涉及总数和启用数校验的方法会抛出 {@link RuntimeException}，由 Controller 层捕获并转换为失败响应。
 */
@Service
public class PortalCarouselServiceImpl
        extends ServiceImpl<PortalCarouselMapper, PortalCarousel>
        implements PortalCarouselService {

    @Override
    public IPage<PortalCarousel> pageByCondition(Integer current, Integer size, Integer status) {
        Page<PortalCarousel> page = new Page<>(current, size);
        return baseMapper.selectPageByCondition(page, status);
    }

    @Override
    public List<PortalCarousel> listByStatus(Integer status) {
        return baseMapper.selectByStatus(status);
    }

    @Override
    public int countByStatus(Integer status) {
        return baseMapper.countByStatus(status);
    }

    @Override
    public int countAll() {
        return baseMapper.countAll();
    }

    /**
     * 更新轮播图状态（启用/停用）
     * <p>
     * 当状态切换为启用（status=1）时，先校验当前启用数是否已达上限（6个）。
     * 若已达上限且该轮播图本身就是启用状态（即状态未变），则允许通过；
     * 若该轮播图当前为停用状态，则拒绝操作。
     *
     * @param id     轮播图主键ID
     * @param status 目标状态：1-启用，0-停用
     * @return 是否更新成功
     * @throws RuntimeException 启用数达上限时抛出
     */
    @Override
    @Transactional
    public boolean updateStatus(Long id, Integer status) {
        if (status == 1) {
            validateBeforeEnable(id);
        }
        return baseMapper.updateStatus(id, status) > 0;
    }

    /**
     * 切换轮播图状态（启用↔停用）
     * <p>
     * 查询当前状态后取反，再调用 {@link #updateStatus(Long, Integer)} 执行更新。
     * 切换为启用时同样会触发启用数上限校验。
     *
     * @param id 轮播图主键ID
     * @return 是否切换成功；记录不存在时返回 false
     */
    @Override
    @Transactional
    public boolean toggleStatus(Long id) {
        PortalCarousel entity = getById(id);
        if (entity == null) {
            return false;
        }
        Integer newStatus = (entity.getStatus() == 1) ? 0 : 1;
        return updateStatus(id, newStatus);
    }

    /**
     * 批量更新轮播图排序
     * <p>
     * 使用 SQL CASE WHEN 语法在单条 UPDATE 语句中完成多行排序更新，
     * 避免了多条 SQL 执行带来的性能开销和事务碎片。
     *
     * @param list 待更新的轮播图列表（需包含 pkId 和 order 字段）
     * @return 是否更新成功
     */
    @Override
    @Transactional
    public boolean updateOrderBatch(List<PortalCarousel> list) {
        return baseMapper.updateOrderBatch(list) > 0;
    }

    /**
     * 新增轮播图前的业务校验
     * <p>
     * 规则：
     * <ol>
     *   <li>总数不得超过 {@link PortalCarouselService#MAX_TOTAL}（10个）</li>
     *   <li>若新增的是启用状态，当前启用数不得超过 {@link PortalCarouselService#MAX_ENABLED}（6个）</li>
     * </ol>
     *
     * @param status 新增轮播图的初始状态：1-启用，0-停用，null-未知
     * @throws RuntimeException 校验不通过时抛出，携带具体上限提示信息
     */
    @Override
    public void validateBeforeSave(Integer status) {
        int total = countAll();
        if (total >= MAX_TOTAL) {
            throw new RuntimeException("轮播图总数已达上限(" + MAX_TOTAL + "个)");
        }
        if (status != null && status == 1) {
            int enabled = countByStatus(1);
            if (enabled >= MAX_ENABLED) {
                throw new RuntimeException("启用中的轮播图已达上限(" + MAX_ENABLED + "个)");
            }
        }
    }

    /**
     * 启用轮播图前的业务校验
     * <p>
     * 与 {@link #validateBeforeSave(Integer)} 的区别：
     * 启用场景下需要考虑"已启用的轮播图再次设置为启用"的情况，
     * 此时启用数未变，应允许通过。只有当启用数已达上限且目标记录当前为停用状态时，才拒绝操作。
     *
     * @param id 待启用的轮播图主键ID
     * @throws RuntimeException 启用数达上限且目标记录当前为停用时抛出
     */
    @Override
    public void validateBeforeEnable(Long id) {
        int enabled = countByStatus(1);
        if (enabled >= MAX_ENABLED) {
            // 查询目标记录的当前状态：若本身已启用，说明启用数未变，允许操作
            PortalCarousel entity = getById(id);
            if (entity != null && entity.getStatus() == 1) {
                return;
            }
            throw new RuntimeException("启用中的轮播图已达上限(" + MAX_ENABLED + "个)");
        }
    }
}

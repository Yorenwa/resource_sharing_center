package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.entity.ResourceManagement;
import org.example.zygl.mapper.ResourceManagementMapper;
import org.example.zygl.service.ResourceManagementService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资源管理服务实现类
 * <p>
 * 资源管理表用于存储门户展示的各类资源信息，支持按分类、上传人、推荐状态、置顶状态等条件查询。
 */
@Service
public class ResourceManagementServiceImpl
        extends ServiceImpl<ResourceManagementMapper, ResourceManagement>
        implements ResourceManagementService {

    @Override
    public IPage<ResourceManagement> pageByCondition(Integer current, Integer size, String resourceName,
                                                     Integer resourceType, Long categoryId, Integer status,
                                                     Integer scope, Integer recommended, Integer pinned) {
        Page<ResourceManagement> page = new Page<>(current, size);
        return baseMapper.selectPageByCondition(page, resourceName, resourceType, categoryId,
                status, scope, recommended, pinned);
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
     * 资源点击量自增
     * <p>
     * 用户浏览或点击资源时调用，通过 SQL UPDATE 实现原子递增，避免并发问题。
     * 仅更新 click_count 字段和 update_time，不触发全量字段更新。
     *
     * @param id 资源主键ID
     * @return 是否更新成功
     */
    @Override
    public boolean addClick(Long id) {
        return baseMapper.addClick(id) > 0;
    }
}

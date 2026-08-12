package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.entity.ResourceManagement;
import org.example.zygl.mapper.ResourceManagementMapper;
import org.example.zygl.service.ResourceManagementService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public boolean addClick(Long id) {
        return baseMapper.addClick(id) > 0;
    }
}

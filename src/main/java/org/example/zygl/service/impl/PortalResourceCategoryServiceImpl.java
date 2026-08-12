package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.entity.PortalResourceCategory;
import org.example.zygl.mapper.PortalResourceCategoryMapper;
import org.example.zygl.service.PortalResourceCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

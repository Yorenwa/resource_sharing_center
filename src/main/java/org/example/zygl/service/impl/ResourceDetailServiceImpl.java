package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.entity.ResourceDetail;
import org.example.zygl.mapper.ResourceDetailMapper;
import org.example.zygl.service.ResourceDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceDetailServiceImpl
        extends ServiceImpl<ResourceDetailMapper, ResourceDetail>
        implements ResourceDetailService {

    @Override
    public List<ResourceDetail> listByResourceId(Long resourceId) {
        return baseMapper.selectByResourceId(resourceId);
    }

    @Override
    public IPage<ResourceDetail> pageByResourceId(Integer current, Integer size, Long resourceId) {
        Page<ResourceDetail> page = new Page<>(current, size);
        return baseMapper.selectPageByResourceId(page, resourceId);
    }
}

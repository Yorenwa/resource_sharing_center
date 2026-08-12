package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.entity.PortalCarousel;
import org.example.zygl.mapper.PortalCarouselMapper;
import org.example.zygl.service.PortalCarouselService;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

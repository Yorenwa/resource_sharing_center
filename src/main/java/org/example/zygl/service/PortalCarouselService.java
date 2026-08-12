package org.example.zygl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.zygl.entity.PortalCarousel;

import java.util.List;

public interface PortalCarouselService extends IService<PortalCarousel> {

    IPage<PortalCarousel> pageByCondition(Integer current, Integer size, Integer status);

    List<PortalCarousel> listByStatus(Integer status);
}

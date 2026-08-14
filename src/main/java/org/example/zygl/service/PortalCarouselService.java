package org.example.zygl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.zygl.entity.PortalCarousel;

import java.util.List;

public interface PortalCarouselService extends IService<PortalCarousel> {

    int MAX_ENABLED = 6;
    int MAX_TOTAL = 10;

    IPage<PortalCarousel> pageByCondition(Integer current, Integer size, Integer status);

    List<PortalCarousel> listByStatus(Integer status);

    int countByStatus(Integer status);

    int countAll();

    boolean updateStatus(Long id, Integer status);

    boolean toggleStatus(Long id);

    boolean updateOrderBatch(List<PortalCarousel> list);

    void validateBeforeSave(Integer status);

    void validateBeforeEnable(Long id);
}

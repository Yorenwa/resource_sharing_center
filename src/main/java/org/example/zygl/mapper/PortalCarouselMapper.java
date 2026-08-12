package org.example.zygl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.zygl.entity.PortalCarousel;

import java.util.List;

@Mapper
public interface PortalCarouselMapper extends BaseMapper<PortalCarousel> {

    IPage<PortalCarousel> selectPageByCondition(IPage<PortalCarousel> page,
                                                 @Param("status") Integer status);

    List<PortalCarousel> selectByStatus(@Param("status") Integer status);

    int countByStatus(@Param("status") Integer status);

    int countAll();

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int updateOrderBatch(@Param("list") List<PortalCarousel> list);
}

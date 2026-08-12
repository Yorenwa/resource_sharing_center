package org.example.zygl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.zygl.entity.ResourceDetail;

import java.util.List;

@Mapper
public interface ResourceDetailMapper extends BaseMapper<ResourceDetail> {

    List<ResourceDetail> selectByResourceId(@Param("resourceId") Long resourceId);

    IPage<ResourceDetail> selectPageByResourceId(IPage<ResourceDetail> page,
                                                  @Param("resourceId") Long resourceId);
}

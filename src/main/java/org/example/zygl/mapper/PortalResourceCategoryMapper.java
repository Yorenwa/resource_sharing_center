package org.example.zygl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.zygl.entity.PortalResourceCategory;

import java.util.List;

@Mapper
public interface PortalResourceCategoryMapper extends BaseMapper<PortalResourceCategory> {

    List<PortalResourceCategory> selectByType(@Param("type") Integer type);

    List<PortalResourceCategory> selectByParentId(@Param("parentId") Long parentId);

    PortalResourceCategory selectByTypeUid(@Param("typeUid") Long typeUid);

    IPage<PortalResourceCategory> selectPageByCondition(IPage<PortalResourceCategory> page,
                                                        @Param("type") Integer type,
                                                        @Param("typeName") String typeName,
                                                        @Param("status") Integer status);

    List<PortalResourceCategory> selectByTypeName(@Param("typeName") String typeName);

    int insertCategory(PortalResourceCategory entity);

    int insertBatch(@Param("list") List<PortalResourceCategory> list);

    int updateCategory(PortalResourceCategory entity);

    int deleteById(@Param("id") Long id);

    int deleteBatchByIds(@Param("ids") List<Long> ids);
}

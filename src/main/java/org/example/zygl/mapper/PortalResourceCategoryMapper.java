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

    /**
     * 查询所有分类（用于树形组装）
     */
    List<PortalResourceCategory> selectAll(@Param("type") Integer type,
                                           @Param("status") Integer status);

    /**
     * 统计指定分类下的子分类数量
     */
    int countChildren(@Param("parentId") Long parentId);

    /**
     * 更新单个分类状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}

package org.example.zygl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.zygl.entity.BrowseHistory;

import java.util.List;

@Mapper
public interface BrowseHistoryMapper extends BaseMapper<BrowseHistory> {

    /**
     * 条件分页查询
     * <p>
     * 支持按用户、资源类型、分类路径（含子分类）、资源名称（模糊）过滤。
     *
     * @param page         分页对象
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @param categoryPath 分类路径（按路径前缀匹配，包含子分类）
     * @param resourceName 资源名称（模糊匹配）
     */
    IPage<BrowseHistory> selectPageByCondition(IPage<BrowseHistory> page,
                                                @Param("userId") Long userId,
                                                @Param("resourceType") Integer resourceType,
                                                @Param("categoryPath") String categoryPath,
                                                @Param("resourceName") String resourceName);

    List<BrowseHistory> selectByUserId(@Param("userId") Long userId);

    List<BrowseHistory> selectByResourceId(@Param("resourceId") Long resourceId);
}

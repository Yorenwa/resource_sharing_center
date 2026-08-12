package org.example.zygl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.zygl.entity.BrowseHistory;

import java.util.List;

@Mapper
public interface BrowseHistoryMapper extends BaseMapper<BrowseHistory> {

    IPage<BrowseHistory> selectPageByCondition(IPage<BrowseHistory> page,
                                                @Param("userId") Long userId,
                                                @Param("resourceType") Integer resourceType,
                                                @Param("categoryId") Long categoryId);

    List<BrowseHistory> selectByUserId(@Param("userId") Long userId);

    List<BrowseHistory> selectByResourceId(@Param("resourceId") Long resourceId);
}

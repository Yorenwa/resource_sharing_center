package org.example.zygl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.zygl.entity.ResourceManagement;

import java.util.List;

@Mapper
public interface ResourceManagementMapper extends BaseMapper<ResourceManagement> {

    IPage<ResourceManagement> selectPageByCondition(IPage<ResourceManagement> page,
                                                     @Param("resourceName") String resourceName,
                                                     @Param("resourceType") Integer resourceType,
                                                     @Param("categoryId") Long categoryId,
                                                     @Param("status") Integer status,
                                                     @Param("scope") Integer scope,
                                                     @Param("recommended") Integer recommended,
                                                     @Param("pinned") Integer pinned);

    List<ResourceManagement> selectByCategoryId(@Param("categoryId") Long categoryId);

    List<ResourceManagement> selectByUploader(@Param("uploader") String uploader);

    List<ResourceManagement> selectRecommended();

    List<ResourceManagement> selectPinned();

    int addClick(@Param("id") Long id);
}

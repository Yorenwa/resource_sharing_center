package org.example.zygl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.zygl.entity.ResourceScopeSchool;

import java.util.List;

@Mapper
public interface ResourceScopeSchoolMapper extends BaseMapper<ResourceScopeSchool> {

    List<ResourceScopeSchool> selectByResourceId(@Param("resourceId") Long resourceId);

    List<ResourceScopeSchool> selectBySchoolUid(@Param("schoolUid") Long schoolUid);
}

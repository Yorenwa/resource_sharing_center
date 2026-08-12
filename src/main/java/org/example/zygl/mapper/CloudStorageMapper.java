package org.example.zygl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.zygl.entity.CloudStorage;

@Mapper
public interface CloudStorageMapper extends BaseMapper<CloudStorage> {

    CloudStorage selectByTenantId(@Param("tenantId") String tenantId);
}

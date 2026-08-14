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

    /**
     * 按资源类型聚合明细存储占用（字节）
     * <p>
     * 通过资源主表关联，获取资源类型，对资源明细中的 resource_storage + attachment_storage 求和。
     * 用于云空间监测的"资源类型分布"饼图。
     *
     * @param resourceType 资源类型（1-视频，2-文档）
     * @param tenantId     租户ID（可选）
     * @return 该类型下所有明细的存储占用总字节数
     */
    Long sumStorageByResourceType(@Param("resourceType") Integer resourceType,
                                   @Param("tenantId") String tenantId);
}

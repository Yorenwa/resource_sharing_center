package org.example.zygl.converter;

import org.example.zygl.dto.ResourceUploadRequest;
import org.example.zygl.entity.ResourceDetail;
import org.example.zygl.entity.ResourceManagement;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 资源上传 DTO ↔ Entity 转换器（MapStruct 自动生成实现）
 */
@Mapper(componentModel = "spring")
public interface ResourceUploadConverter {

    ResourceUploadConverter INSTANCE = Mappers.getMapper(ResourceUploadConverter.class);

    /**
     * 将上传请求体的公共字段映射为资源主表实体
     * <p>
     * 忽略由服务层统一填充的字段：
     * <ul>
     *   <li>pkId：主键自增</li>
     *   <li>status / recommended / pinned / clicks：默认值由服务层通过枚举设置</li>
     *   <li>creator / createTime / updateTime / deleted：服务层统一填充</li>
     * </ul>
     */
    @Mapping(target = "pkId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "recommended", ignore = true)
    @Mapping(target = "pinned", ignore = true)
    @Mapping(target = "clicks", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updater", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "resourceName", ignore = true)
    @Mapping(target = "resourceUrl", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    ResourceManagement toEntity(ResourceUploadRequest source);

    /**
     * 将明细项映射为资源明细实体
     * <p>
     * resourceId 由服务层在调用后设置。
     */
    @Mapping(target = "pkId", ignore = true)
    @Mapping(target = "resourceId", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    ResourceDetail toDetailEntity(ResourceUploadRequest.ResourceDetailUploadItem item);

    /**
     * 批量将明细项映射为明细实体列表
     */
    List<ResourceDetail> toDetailEntities(List<ResourceUploadRequest.ResourceDetailUploadItem> items);

    /**
     * 明细映射后填充集号默认值（若未指定则按顺序自增）
     */
    @AfterMapping
    default void fillDefaultEpisodeNo(ResourceUploadRequest.ResourceDetailUploadItem item,
                                       @MappingTarget ResourceDetail detail) {
        if (detail.getEpisodeNo() == null) {
            // 临时占位，实际由服务层按顺序覆盖
            detail.setEpisodeNo(0);
        }
    }
}

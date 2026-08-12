package org.example.zygl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.zygl.entity.PortalResourceCategory;

import java.util.List;

public interface PortalResourceCategoryService extends IService<PortalResourceCategory> {

    List<PortalResourceCategory> listByType(Integer type);

    List<PortalResourceCategory> listByParentId(Long parentId);

    PortalResourceCategory getByTypeUid(Long typeUid);

    IPage<PortalResourceCategory> pageByCondition(Integer current, Integer size, Integer type, String typeName, Integer status);

    List<PortalResourceCategory> listByTypeName(String typeName);
}

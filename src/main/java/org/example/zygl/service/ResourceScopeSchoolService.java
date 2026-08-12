package org.example.zygl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.zygl.entity.ResourceScopeSchool;

import java.util.List;

public interface ResourceScopeSchoolService extends IService<ResourceScopeSchool> {

    List<ResourceScopeSchool> listByResourceId(Long resourceId);

    List<ResourceScopeSchool> listBySchoolUid(Long schoolUid);
}

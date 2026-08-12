package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.entity.ResourceScopeSchool;
import org.example.zygl.mapper.ResourceScopeSchoolMapper;
import org.example.zygl.service.ResourceScopeSchoolService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceScopeSchoolServiceImpl
        extends ServiceImpl<ResourceScopeSchoolMapper, ResourceScopeSchool>
        implements ResourceScopeSchoolService {

    @Override
    public List<ResourceScopeSchool> listByResourceId(Long resourceId) {
        return baseMapper.selectByResourceId(resourceId);
    }

    @Override
    public List<ResourceScopeSchool> listBySchoolUid(Long schoolUid) {
        return baseMapper.selectBySchoolUid(schoolUid);
    }
}

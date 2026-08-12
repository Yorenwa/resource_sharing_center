package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.entity.CloudStorage;
import org.example.zygl.mapper.CloudStorageMapper;
import org.example.zygl.service.CloudStorageService;
import org.springframework.stereotype.Service;

@Service
public class CloudStorageServiceImpl
        extends ServiceImpl<CloudStorageMapper, CloudStorage>
        implements CloudStorageService {

    @Override
    public CloudStorage getByTenantId(String tenantId) {
        return baseMapper.selectByTenantId(tenantId);
    }
}

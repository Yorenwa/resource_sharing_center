package org.example.zygl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.zygl.entity.CloudStorage;

public interface CloudStorageService extends IService<CloudStorage> {

    CloudStorage getByTenantId(String tenantId);
}

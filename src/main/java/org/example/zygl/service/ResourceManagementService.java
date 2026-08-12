package org.example.zygl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.zygl.entity.ResourceManagement;

import java.util.List;

public interface ResourceManagementService extends IService<ResourceManagement> {

    IPage<ResourceManagement> pageByCondition(Integer current, Integer size, String resourceName,
                                              Integer resourceType, Long categoryId, Integer status,
                                              Integer scope, Integer recommended, Integer pinned);

    List<ResourceManagement> listByCategoryId(Long categoryId);

    List<ResourceManagement> listByUploader(String uploader);

    List<ResourceManagement> listRecommended();

    List<ResourceManagement> listPinned();

    boolean addClick(Long id);
}

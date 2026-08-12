package org.example.zygl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.zygl.entity.ResourceDetail;

import java.util.List;

public interface ResourceDetailService extends IService<ResourceDetail> {

    List<ResourceDetail> listByResourceId(Long resourceId);

    IPage<ResourceDetail> pageByResourceId(Integer current, Integer size, Long resourceId);
}

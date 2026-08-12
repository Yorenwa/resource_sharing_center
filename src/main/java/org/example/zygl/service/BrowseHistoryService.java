package org.example.zygl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.zygl.entity.BrowseHistory;

import java.util.List;

public interface BrowseHistoryService extends IService<BrowseHistory> {

    IPage<BrowseHistory> pageByCondition(Integer current, Integer size, Long userId,
                                         Integer resourceType, Long categoryId);

    List<BrowseHistory> listByUserId(Long userId);

    List<BrowseHistory> listByResourceId(Long resourceId);
}

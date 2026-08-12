package org.example.zygl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.zygl.entity.BrowseHistory;
import org.example.zygl.mapper.BrowseHistoryMapper;
import org.example.zygl.service.BrowseHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrowseHistoryServiceImpl
        extends ServiceImpl<BrowseHistoryMapper, BrowseHistory>
        implements BrowseHistoryService {

    @Override
    public IPage<BrowseHistory> pageByCondition(Integer current, Integer size, Long userId,
                                                Integer resourceType, Long categoryId) {
        Page<BrowseHistory> page = new Page<>(current, size);
        return baseMapper.selectPageByCondition(page, userId, resourceType, categoryId);
    }

    @Override
    public List<BrowseHistory> listByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public List<BrowseHistory> listByResourceId(Long resourceId) {
        return baseMapper.selectByResourceId(resourceId);
    }
}

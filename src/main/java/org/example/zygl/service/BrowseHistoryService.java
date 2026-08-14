package org.example.zygl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.zygl.entity.BrowseHistory;

import java.util.List;

/**
 * 浏览记录服务接口
 * <p>
 * 核心业务规则：
 * <ul>
 *   <li>分页查询支持按用户、资源类型、分类路径（含子分类）、资源名称（模糊）过滤</li>
 *   <li>返回结果自动填充 categoryNamePath（分类名称路径，如 "区级安全宣传/安全制度"）</li>
 *   <li>浏览时间按倒序排列，最新浏览排在最前</li>
 * </ul>
 */
public interface BrowseHistoryService extends IService<BrowseHistory> {

    /**
     * 条件分页查询
     * <p>
     * 支持按用户、资源类型、分类路径、资源名称过滤，结果自动填充分类名称路径。
     *
     * @param current      当前页
     * @param size         每页大小
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @param categoryPath 分类路径（按路径前缀匹配，包含子分类）
     * @param resourceName 资源名称（模糊匹配）
     */
    IPage<BrowseHistory> pageByCondition(Integer current, Integer size, Long userId,
                                         Integer resourceType, String categoryPath,
                                         String resourceName);

    List<BrowseHistory> listByUserId(Long userId);

    List<BrowseHistory> listByResourceId(Long resourceId);
}

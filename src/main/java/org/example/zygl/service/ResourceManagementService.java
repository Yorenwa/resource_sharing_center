package org.example.zygl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.zygl.dto.CategoryStatDTO;
import org.example.zygl.dto.ResourceUploadRequest;
import org.example.zygl.entity.ResourceManagement;

import java.util.List;

/**
 * 资源管理服务接口
 * <p>
 * 业务规则：
 * <ul>
 *   <li>单个一级分类下最多 4 个置顶资源</li>
 *   <li>审核状态：1-待审核，2-已通过，3-已驳回</li>
 *   <li>推荐/置顶资源可在列表页通过 tab 切换筛选</li>
 * </ul>
 */
public interface ResourceManagementService extends IService<ResourceManagement> {

    /**
     * 条件分页查询（支持一级/二级分类、上传人、学校等多条件）
     */
    IPage<ResourceManagement> pageByCondition(Integer current, Integer size, String resourceName,
                                              Integer resourceType, Long categoryId,
                                              Long level1CategoryId, Long level2CategoryId,
                                              Integer status, Integer scope,
                                              Integer recommended, Integer pinned,
                                              String uploader, Long schoolUid);

    List<ResourceManagement> listByCategoryId(Long categoryId);

    List<ResourceManagement> listByUploader(String uploader);

    List<ResourceManagement> listRecommended();

    List<ResourceManagement> listPinned();

    /**
     * 资源点击量自增
     */
    boolean addClick(Long id);

    /**
     * 获取分类统计信息（全部资源数、置顶数、推荐数）
     *
     * @param categoryPath 分类路径（可选）
     * @return 统计结果 DTO
     */
    CategoryStatDTO categoryStats(String categoryPath);

    /**
     * 设置推荐状态
     *
     * @param id          资源ID
     * @param recommended 1-推荐，0-取消推荐
     */
    boolean updateRecommended(Long id, Integer recommended);

    /**
     * 设置置顶状态（含置顶上限校验）
     * <p>
     * 单个一级分类下最多 4 个置顶资源，超限时拒绝置顶。
     *
     * @param id     资源ID
     * @param pinned 1-置顶，0-取消置顶
     * @throws RuntimeException 超过上限时抛出
     */
    boolean updatePinned(Long id, Integer pinned);

    /**
     * 切换推荐状态
     */
    boolean toggleRecommended(Long id);

    /**
     * 切换置顶状态
     */
    boolean togglePinned(Long id);

    /**
     * 批量设置推荐状态
     */
    boolean batchUpdateRecommended(List<Long> ids, Integer recommended);

    /**
     * 批量设置置顶状态（含置顶上限校验）
     *
     * @throws RuntimeException 超过上限时抛出
     */
    boolean batchUpdatePinned(List<Long> ids, Integer pinned);

    /**
     * 审核通过
     */
    boolean approve(Long id);

    /**
     * 审核驳回
     */
    boolean reject(Long id, String rejectionReason);

    /**
     * 批量审核通过
     */
    boolean batchApprove(List<Long> ids);

    /**
     * 批量审核驳回
     */
    boolean batchReject(List<Long> ids, String rejectionReason);

    /**
     * 保存资源及明细（上传流程）
     * <p>
     * 先保存资源主表，再批量保存资源明细，全程事务保护。
     *
     * @param request 上传请求体
     * @return 资源主表 ID
     */
    Long saveWithDetails(ResourceUploadRequest request);
}

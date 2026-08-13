package org.example.zygl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.zygl.entity.ResourceManagement;

import org.example.zygl.dto.CategoryStatDTO;

import java.util.List;

@Mapper
public interface ResourceManagementMapper extends BaseMapper<ResourceManagement> {

    /**
     * 条件分页查询（支持一级/二级分类、学校、上传人等多条件过滤）
     */
    IPage<ResourceManagement> selectPageByCondition(IPage<ResourceManagement> page,
                                                     @Param("resourceName") String resourceName,
                                                     @Param("resourceType") Integer resourceType,
                                                     @Param("categoryId") Long categoryId,
                                                     @Param("level1CategoryId") Long level1CategoryId,
                                                     @Param("level2CategoryId") Long level2CategoryId,
                                                     @Param("status") Integer status,
                                                     @Param("scope") Integer scope,
                                                     @Param("recommended") Integer recommended,
                                                     @Param("pinned") Integer pinned,
                                                     @Param("uploader") String uploader,
                                                     @Param("schoolUid") Long schoolUid);

    List<ResourceManagement> selectByCategoryId(@Param("categoryId") Long categoryId);

    List<ResourceManagement> selectByUploader(@Param("uploader") String uploader);

    List<ResourceManagement> selectRecommended();

    List<ResourceManagement> selectPinned();

    /**
     * 资源点击量原子递增
     */
    int addClick(@Param("id") Long id);

    /**
     * 统计指定分类下的资源数量
     */
    int countByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 统计指定分类下已置顶资源数量
     */
    int countPinnedByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 统计推荐资源数量
     */
    int countRecommended();

    /**
     * 切换推荐状态
     */
    int updateRecommended(@Param("id") Long id, @Param("recommended") Integer recommended);

    /**
     * 切换置顶状态
     */
    int updatePinned(@Param("id") Long id, @Param("pinned") Integer pinned);

    /**
     * 批量设置推荐状态
     */
    int batchUpdateRecommended(@Param("ids") List<Long> ids, @Param("recommended") Integer recommended);

    /**
     * 批量设置置顶状态
     */
    int batchUpdatePinned(@Param("ids") List<Long> ids, @Param("pinned") Integer pinned);

    /**
     * 更新审核状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status,
                     @Param("rejectionReason") String rejectionReason);

    /**
     * 批量更新审核状态
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status,
                          @Param("rejectionReason") String rejectionReason);

    /**
     * 统计分类下的全部资源数（含子分类）
     */
    int countByCategoryPath(@Param("categoryPath") String categoryPath);

    /**
     * 统计分类下已置顶资源数（含子分类）
     */
    int countPinnedByCategoryPath(@Param("categoryPath") String categoryPath);

    /**
     * 分类资源统计
     */
    CategoryStatDTO categoryStats(@Param("categoryPath") String categoryPath);
}

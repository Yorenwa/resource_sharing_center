package org.example.zygl.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 门户资源分类树形节点
 * <p>
 * 用于前端展示层级结构（一级分类 → 二级分类）。
 * 继承自 PortalResourceCategory 的字段，额外增加 children 子节点列表。
 */
@Data
public class PortalResourceCategoryNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long pkId;

    private Long typeUid;

    private String typeName;

    private Long parentId;

    private Integer level;

    private String path;

    /**
     * 名称路径（查询时动态计算，非数据库字段）
     * <p>
     * 例如：区级安全宣传/政策法规
     */
    private String namePath;

    private Integer type;

    private Double order;

    private Integer status;

    private String creator;

    private String updater;

    private String tenantId;

    private List<PortalResourceCategoryNode> children = new ArrayList<>();
}

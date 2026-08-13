package org.example.zygl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源参与范围枚举
 */
@Getter
@AllArgsConstructor
public enum ResourceScopeEnum {

    /**
     * 公开
     */
    PUBLIC(1, "公开"),

    /**
     * 指定范围
     */
    SPECIFIED(2, "指定范围");

    private final int code;
    private final String desc;
}

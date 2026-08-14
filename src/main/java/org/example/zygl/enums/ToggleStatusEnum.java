package org.example.zygl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 开关状态枚举（推荐、置顶等通用）
 */
@Getter
@AllArgsConstructor
public enum ToggleStatusEnum {

    /**
     * 关闭
     */
    OFF(0, "关闭"),

    /**
     * 开启
     */
    ON(1, "开启");

    private final int code;
    private final String desc;
}

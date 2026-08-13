package org.example.zygl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源类型枚举
 */
@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {

    /**
     * 视频
     */
    VIDEO(1, "视频"),

    /**
     * 文档
     */
    DOCUMENT(2, "文档");

    private final int code;
    private final String desc;
}

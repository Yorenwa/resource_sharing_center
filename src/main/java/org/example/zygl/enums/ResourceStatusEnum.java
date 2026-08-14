package org.example.zygl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源审核状态枚举
 */
@Getter
@AllArgsConstructor
public enum ResourceStatusEnum {

    /**
     * 待审核
     */
    PENDING(1, "待审核"),

    /**
     * 已通过
     */
    APPROVED(2, "已通过"),

    /**
     * 已驳回
     */
    REJECTED(3, "已驳回");

    private final int code;
    private final String desc;

    public static ResourceStatusEnum fromCode(int code) {
        for (ResourceStatusEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("无效的审核状态码：" + code);
    }
}

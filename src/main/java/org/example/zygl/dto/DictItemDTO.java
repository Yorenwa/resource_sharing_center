package org.example.zygl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 字典项 DTO
 * <p>
 * 用于前端下拉选择的统一数据结构。
 *
 * @param <T> value 的类型（String 或 Integer）
 */
@Data
@AllArgsConstructor
public class DictItemDTO<T> {

    /**
     * 选项值
     */
    private T value;

    /**
     * 选项标签
     */
    private String label;
}

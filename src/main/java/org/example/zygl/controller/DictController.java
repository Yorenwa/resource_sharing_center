package org.example.zygl.controller;

import org.example.zygl.dto.DictItemDTO;
import org.example.zygl.enums.ResourceScopeEnum;
import org.example.zygl.enums.ResourceTypeEnum;
import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 数据字典接口
 * <p>
 * 提供前端下拉选择所需的静态/半静态数据，如学段、资源类型等。
 */
@RestController
@RequestMapping("/api/dict")
public class DictController {

    /**
     * 学段列表
     * <p>
     * 对应前端"学段"下拉选择。
     */
    @GetMapping("/school-stage")
    public R<List<DictItemDTO<String>>> listSchoolStage() {
        List<DictItemDTO<String>> stages = Arrays.asList(
                new DictItemDTO<>("primary", "小学"),
                new DictItemDTO<>("junior", "初中"),
                new DictItemDTO<>("senior", "高中"),
                new DictItemDTO<>("vocational", "职业教育"),
                new DictItemDTO<>("higher", "高等教育"),
                new DictItemDTO<>("preschool", "学前教育"),
                new DictItemDTO<>("special", "特殊教育")
        );
        return R.ok(stages);
    }

    /**
     * 资源类型列表
     * <p>
     * 对应前端"资源类型"下拉选择。
     */
    @GetMapping("/resource-type")
    public R<List<DictItemDTO<Integer>>> listResourceType() {
        List<DictItemDTO<Integer>> types = Arrays.asList(
                new DictItemDTO<>(ResourceTypeEnum.VIDEO.getCode(), ResourceTypeEnum.VIDEO.getDesc()),
                new DictItemDTO<>(ResourceTypeEnum.DOCUMENT.getCode(), ResourceTypeEnum.DOCUMENT.getDesc())
        );
        return R.ok(types);
    }

    /**
     * 参与范围列表
     */
    @GetMapping("/scope")
    public R<List<DictItemDTO<Integer>>> listScope() {
        List<DictItemDTO<Integer>> scopes = Arrays.asList(
                new DictItemDTO<>(ResourceScopeEnum.PUBLIC.getCode(), ResourceScopeEnum.PUBLIC.getDesc()),
                new DictItemDTO<>(ResourceScopeEnum.SPECIFIED.getCode(), ResourceScopeEnum.SPECIFIED.getDesc())
        );
        return R.ok(scopes);
    }
}

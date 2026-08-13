package org.example.zygl.controller;

import org.example.zygl.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public R<List<Map<String, String>>> listSchoolStage() {
        List<Map<String, String>> stages = new ArrayList<>();
        stages.add(buildItem("primary", "小学"));
        stages.add(buildItem("junior", "初中"));
        stages.add(buildItem("senior", "高中"));
        stages.add(buildItem("vocational", "职业教育"));
        stages.add(buildItem("higher", "高等教育"));
        stages.add(buildItem("preschool", "学前教育"));
        stages.add(buildItem("special", "特殊教育"));
        return R.ok(stages);
    }

    /**
     * 资源类型列表
     * <p>
     * 对应前端"资源类型"下拉选择。
     */
    @GetMapping("/resource-type")
    public R<List<Map<String, Object>>> listResourceType() {
        List<Map<String, Object>> types = new ArrayList<>();
        types.add(buildTypeItem(1, "视频"));
        types.add(buildTypeItem(2, "文档"));
        return R.ok(types);
    }

    /**
     * 参与范围列表
     */
    @GetMapping("/scope")
    public R<List<Map<String, Object>>> listScope() {
        List<Map<String, Object>> scopes = new ArrayList<>();
        scopes.add(buildTypeItem(1, "公开"));
        scopes.add(buildTypeItem(2, "指定范围"));
        return R.ok(scopes);
    }

    private Map<String, String> buildItem(String value, String label) {
        Map<String, String> item = new HashMap<>();
        item.put("value", value);
        item.put("label", label);
        return item;
    }

    private Map<String, Object> buildTypeItem(int value, String label) {
        Map<String, Object> item = new HashMap<>();
        item.put("value", value);
        item.put("label", label);
        return item;
    }
}

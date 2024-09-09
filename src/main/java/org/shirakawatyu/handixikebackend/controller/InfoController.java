package org.shirakawatyu.handixikebackend.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.shirakawatyu.handixikebackend.common.Constants;
import org.shirakawatyu.handixikebackend.utils.DateUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 常用信息获取
 *
 * @author ShirakawaTyu
 * @date 2024/03/25
 */
@RestController
public class InfoController {
    /**
     * 获取周
     *
     * @return {@code String}
     */
    @GetMapping("/api/week")
    public String getWeek() {
        JSONObject week = new JSONObject();
        week.put("cur", DateUtil.curWeek());
        week.put("total", DateUtil.totalWeek());
        week.put("startDate", Long.toString(Constants.START_DATE));
        return JSON.toJSONString(week);
    }

    /**
     * 获取版本信息
     *
     * @return {@code String}
     */
    @GetMapping("/api/web/version")
    public String getVersion() {
        return Constants.WEB_VERSION;
    }
}

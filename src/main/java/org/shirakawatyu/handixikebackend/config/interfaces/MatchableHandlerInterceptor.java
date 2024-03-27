package org.shirakawatyu.handixikebackend.config.interfaces;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.List;

/**
 * 匹配 HandlerInterceptor
 *
 * @author HuYuanYang
 * @since  2024/03/27
 */
public interface MatchableHandlerInterceptor extends HandlerInterceptor {

    /**
     * 包括路径
     *
     * @return {@code List<String>}
     */
    default List<String> includes() {
        return Collections.emptyList();
    }

    /**
     * 排除路径
     *
     * @return {@code List<String>}
     */
    default List<String> excludes() {
        return Collections.emptyList();
    }
}

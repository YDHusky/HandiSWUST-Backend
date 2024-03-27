package org.shirakawatyu.handixikebackend.config.interfaces;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.List;

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

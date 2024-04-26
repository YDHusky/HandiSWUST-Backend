package org.shirakawatyu.handixikebackend.common;

import lombok.experimental.UtilityClass;

/**
 * @author ShirakawaTyu
 */
@UtilityClass
public class ResultCode {
    // 认证相关
    public static final int LOGIN_FAIL = 1500;
    public static final int REMOTE_SERVICE_ERROR = 1502;
    public static final int LOGIN_SUCCESS = 1200;
    public static final int LOGOUT_SUCCESS = 2200;
    public static final int HAS_LOGIN = 3200;
    public static final int LOGOUT = 3401;
    public static final int OUT_OF_CREDIT = 3403;
    public static final int TIMEOUT = 5501;
    public static final int SERVER_CLOSE = 5502;
}

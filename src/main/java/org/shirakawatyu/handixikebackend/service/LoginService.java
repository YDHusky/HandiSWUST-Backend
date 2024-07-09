package org.shirakawatyu.handixikebackend.service;

import org.shirakawatyu.handixikebackend.common.Result;


public interface LoginService {

    Result getKey();

    Result getCaptcha();

    Result login(String username, String password, String captcha);

    Result logout();

    Result loginCheck();

    String getDynamicCode(String phone);

    Result loginByPhone(String phone, String code);
}

package org.shirakawatyu.handixikebackend.api;

import org.apache.hc.client5.http.cookie.CookieStore;

import java.util.Map;

public interface LoginApi {
    Map<String, String> getKey(CookieStore cookieStore);

    byte[] getCaptcha(CookieStore cookieStore);

    int login(String username, String password, String captcha, CookieStore cookieStore);

    boolean logout(CookieStore cookieStore);
}

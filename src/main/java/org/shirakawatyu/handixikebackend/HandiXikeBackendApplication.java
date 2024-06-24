package org.shirakawatyu.handixikebackend;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.shirakawatyu.handixikebackend.utils.Requests;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.logging.Logger;

/**
 * @author ShirakawaTyu
 */
@SpringBootApplication
@EnableScheduling
public class HandiXikeBackendApplication {

    public static void main(String[] args) {
        try {
            String ip = Requests.getForString("https://ipinfo.io/ip", "", new BasicCookieStore());
            Logger.getLogger("HandiXikeBackendApplication").info("当前服务器IP地址：" + ip);
        } catch (Exception ignored) {}
        SpringApplication.run(HandiXikeBackendApplication.class, args);
    }

}

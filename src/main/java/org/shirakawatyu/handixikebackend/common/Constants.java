package org.shirakawatyu.handixikebackend.common;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.shirakawatyu.handixikebackend.utils.DateUtil;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import static org.shirakawatyu.handixikebackend.common.Constants.Props.INSTANCE;

@Slf4j
@UtilityClass
public class Constants {
    /**
     * cur.term
     */
    public static String CURRENT_TERM = INSTANCE.key("cur.term", "2024-2025-2");
    /**
     * cur.term.long
     */
    public static long CURRENT_TERM_LONG = INSTANCE.key("cur.term.long", 202420252L);    // 2023-2024-1
    /**
     * start.date
     */
    public static long START_DATE = INSTANCE.key("start.date", DateUtil.getDate("2025-2-17"));
    /**
     * end.date
     */
    public static long END_DATE = INSTANCE.key("end.date", DateUtil.getDate("2025-6-14"));
    /**
     * 网络版本
     */
    public static final String WEB_VERSION = "1.0";

    @Component
    public class Strap implements SmartInitializingSingleton {
        @Override
        public void afterSingletonsInstantiated() {
            INSTANCE.init();
        }
    }

    public enum Props {
        INSTANCE;
        private final StringRedisTemplate template = SpringUtil.getBean(StringRedisTemplate.class);

        public void init() {
            log.info("start prop listener ...");
            RedisConnectionFactory factory = template.getConnectionFactory();
            RedisMessageListenerContainer container = new RedisMessageListenerContainer();
            container.setConnectionFactory(factory);
            container.addMessageListener((msg, pattern) -> {
                String val = new String(msg.getBody());
                log.info("receive {}", msg);
                String[] key = val.split(":");
                if (key.length > 1) {
                    dispatch(key[0], key[1]);
                }
            }, new ChannelTopic("props"));
            container.afterPropertiesSet();
            container.start();
        }


        @SuppressWarnings("unchecked")
        private <T> T get(Object o, Object defaultValue) {
            return (T) Convert.convert(defaultValue.getClass(), o, defaultValue);
        }

        public <T> T key(String key, Object defaultValue) {
            String val = template.opsForValue().get(key);
            return get(val, defaultValue);
        }

        private void dispatch(String key, String val) {
            log.info("dispatch key: {}, val: {}", key, val);
            template.opsForValue().set(key, val);
            switch (key) {
                case "cur.term":
                    CURRENT_TERM = key(key, CURRENT_TERM);
                    break;
                case "cur.term.long":
                    CURRENT_TERM_LONG = key(key, CURRENT_TERM_LONG);
                    break;
                case "start.date":
                    START_DATE = key(key, START_DATE);
                    break;
                case "end.date":
                    END_DATE = key(key, END_DATE);
                    break;
            }
        }
    }
}

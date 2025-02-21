package org.shirakawatyu.handixikebackend.cache.impl;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.shirakawatyu.handixikebackend.api.CourseApi;
import org.shirakawatyu.handixikebackend.api.impl.NormalCourseApi;
import org.shirakawatyu.handixikebackend.cache.RawCourseCache;
import org.shirakawatyu.handixikebackend.pojo.Lesson;
import org.shirakawatyu.handixikebackend.utils.ArrayUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author ShirakawaTyu
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RawCourseCacheImpl implements RawCourseCache {

    private final StringRedisTemplate redisTemplate;
    @Resource(name = "NormalCourseApi")
    private NormalCourseApi normalCourseApi;
    @Resource(name = "ExperimentCourseApi")
    private CourseApi experimentCourseApi;


    @Scheduled(cron = "0 0 1 * * ? ")
    @CacheEvict(value = "Course", allEntries = true)
    @Override
    public void deleteCache() {
//        TODO 异步unlink 减少slow log (有用但不多
//        ScanOptions options = KeyScanOptions.scanOptions()
//                .match("Course::*")
//                .count(1000)
//                .build();
//        new ArrayList<>();
//
//        try (Cursor<String> cursor = redisTemplate.scan(options)) {
//            while (cursor.hasNext()) {
//                redisTemplate.unlink(cursor.next());
//            }
//        }
        log.info("已清理缓存");
    }

    @Override
    public boolean manualDeleteCache(String no) {
        try {
            Set<String> keys = redisTemplate.keys("*" + no + "*");
            if (keys != null) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Cacheable(value = "Course", key = "'e'+#p1", unless = "null == #result")
    @Override
    public List<Lesson> getExperimentCourse(CookieStore cookieStore, String no) {
        List<Lesson> course = experimentCourseApi.getCourse(cookieStore);
        ArrayUtils.nullObjChk(course);
        return course;
    }

    @Cacheable(value = "Course", key = "'n'+#p1", unless = "null == #result")
    @Override
    public List<Lesson> getNormalCourse(CookieStore cookieStore, String no) {
        List<Lesson> course = normalCourseApi.getCourse(cookieStore);
        ArrayUtils.nullObjChk(course);
        return course;
    }
}

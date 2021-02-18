package com.haozi.cache.demo.crud;

import com.haozi.cache.core.AutoCache;
import com.haozi.cache.core.AutoCacheEvict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * demo cache
 *
 * @author haozi
 */
@Slf4j
@Service
public class TeacherService {

    @AutoCache(localTTL = 5, remoteTTL = 30, cacheName = "teacher")
    public Teacher getTeacher(Long id) {
        log.info("getTeacher id {}", id);
        return Teacher.builder()
                .id(id)
                .name(id + "老师")
                .build();
    }

    @AutoCache(localTTL = 5, remoteTTL = 30, cacheName = "teacher", elementCache = true)
    public List<Teacher> getTeacher(List<Long> ids) {
        log.info("getTeacher ids {}", ids);
        return ids.stream()
                .map(id -> Teacher.builder()
                        .id(id)
                        .name(id + "老师")
                        .build())
                .collect(Collectors.toList());

    }

    @AutoCacheEvict(cacheName = "teacher")
    public Integer del(Long id) {
        log.info("del id {}", id);

        return 1;
    }

    @AutoCacheEvict(cacheName = "teacher", key = "#teacher.id")
    public Integer update(Teacher teacher) {
        log.info("update teacher {}", teacher);
        return 1;
    }
}

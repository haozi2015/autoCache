package com.haozi.cache.demo;

import com.haozi.cache.demo.service.Teacher;
import com.haozi.cache.demo.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * demo
 *
 * @author haozi
 */
@Slf4j
@Component
public class TeacherCacheDemo implements InitializingBean {
    @Resource
    private TeacherService teacherService;

    /**
     * 单个对象缓存
     *
     * @throws InterruptedException
     */
    private void get() throws InterruptedException {
        /**
         * 第1次查，回源
         */
        teacherService.getTeacher(1L);
        /**
         * 第2次查，内存local缓存
         */
        teacherService.getTeacher(1L);
        /**
         * 第3次查，内存缓存过期，远程remote缓存
         */
        Thread.sleep(5L);
        teacherService.getTeacher(1L);
        /**
         * 第4次查，缓存过期，回源
         */
        Thread.sleep(30L);
        teacherService.getTeacher(1L);
    }

    /**
     * 批量缓存
     *
     * @throws InterruptedException
     */
    private void batchGet() throws InterruptedException {
        /**
         * 第1次查，回源
         */
        teacherService.getTeacher(Arrays.asList(1L, 2L));
        /**
         * 第2次查，命中缓存
         */
        teacherService.getTeacher(Arrays.asList(1L, 2L));
        /**
         * 第3次查，部分命中缓存，未命中部分回源
         */
        teacherService.getTeacher(Arrays.asList(1L, 2L, 3L));
    }

    /**
     * 清除缓存
     */
    private void evict() {
        /**
         * 第1次，删除逻辑，自动清除缓存
         */
        teacherService.del(1L);
        /**
         * 第2次查，回源
         */
        teacherService.getTeacher(1L);
        /**
         * 第3次查，命中缓存
         */
        teacherService.getTeacher(1L);
        /**
         * 第4次，删除逻辑，自动清除缓存
         */
        teacherService.del(1L);
        /**
         * 第5次查，回源
         */
        teacherService.getTeacher(1L);
    }

    /**
     * 清除缓存
     */
    private void evict2() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .name("大老师")
                .build();
        /**
         * 第1次，更新逻辑，自动清除缓存
         */
        teacherService.update(teacher);
        /**
         * 第2次查，回源
         */
        teacherService.getTeacher(1L);
        /**
         * 第3次查，命中缓存
         */
        teacherService.getTeacher(1L);
        /**
         * 第4次，更新逻辑，自动清除缓存
         */
        teacherService.update(teacher);
        /**
         * 第5次查，回源
         */
        teacherService.getTeacher(1L);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info(" demo ----get-----");
        get();

        log.info(" demo ----evict-----");
        evict();

        log.info(" demo ----evict2-----");
        evict2();
    }
}

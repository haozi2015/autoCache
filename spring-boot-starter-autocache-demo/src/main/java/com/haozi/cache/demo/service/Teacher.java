package com.haozi.cache.demo.service;

import com.haozi.cache.core.AutoCacheField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * demo对象
 *
 * @author haozi
 * @date 2020/11/133:20 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @AutoCacheField
    private Long id;
    private String name;
}

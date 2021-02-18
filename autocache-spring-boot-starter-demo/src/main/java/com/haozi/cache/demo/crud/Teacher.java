package com.haozi.cache.demo.crud;

import com.haozi.cache.core.AutoCacheField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * demo对象
 *
 * @author haozi
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

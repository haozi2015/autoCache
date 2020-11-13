package com.haozi.cache.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动缓存
 * <p>
 * 注解直接作用在接口时，不能使用{@code key}的SpEL表达式定义key值，
 * 如果是动态代理出来的类，是拿不到方法参数名的。
 * <p>
 * 返回基础类型、String、对象、key未指定，默认是参数
 * <p>
 * 返回集合，若元素是对象且对象中属性声明了AutoCacheField注解，key为此参数值
 *
 * @author haozi
 * @date 2020/6/410:28 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AutoCache {
    /**
     * 本地缓存过期时间
     * <p>
     * 单位：秒
     * <p>
     * 不缓存null结果
     *
     * @return
     */
    long localTTL() default 0;

    /**
     * 远程缓存过期时间
     * <p>
     * 单位：秒
     * <p>
     * 缓存null结果
     *
     * @return
     */
    long remoteTTL() default 0;

    /**
     * 自定义key
     * <p>
     * 支持Spring Expression Language (SpEL)解析表达式
     * <p>
     * 未定义key时，默认参数值':'拼接；
     * <p>
     * 特殊场景：如果返回对象class中的属性声明{@link AutoCacheField},则使用声明字段值为key；
     *
     * @return
     */
    String key() default "";

    /**
     * 是否支持缓存元素
     * <p>
     * 元素要求：
     * 必须为对象，且实现{@link java.io.Serializable}接口，属性声明{@link AutoCacheField}注解
     *
     * @return
     */
    boolean elementCache() default false;

    /**
     * 缓存名称
     * <p>
     * 用于隔离和删除
     * <p>
     * 默认值：声明{@link AutoCache}注解的方法类名+方法名
     *
     * @return
     */
    String cacheName() default "";

}

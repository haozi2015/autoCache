package com.haozi.cache.demo;

import com.haozi.cache.core.AutoCache;
import org.springframework.stereotype.Component;

/**
 * 缓存使用Demo
 *
 * @author haozi
 */
@Component
public class SimpleAutoCacheDemo {
    /**
     * 仅用本地缓存
     *
     * @return string
     */
    @AutoCache(localTTL = 5)
    public String getLocalStr() {
        return "abc";
    }

    /**
     * 限定内存缓存最大条数
     *
     * @return
     */
    @AutoCache(localTTL = 5, localMaxSize = 1000)
    public String getLocalStr2() {
        return "abc";
    }

    /**
     * 仅用远程缓存
     *
     * @return string
     */
    @AutoCache(remoteTTL = 30)
    public String getRemoteStr() {
        return "abc";
    }

    /**
     * 二级缓存
     * <p>
     * 先查本地缓存，不存在时查远程缓存，不存在时调用原方法，结果再依次被远程和本地缓存
     *
     * @return string
     */
    @AutoCache(localTTL = 5, remoteTTL = 30)
    public String getStr() {
        return "abc";
    }

}

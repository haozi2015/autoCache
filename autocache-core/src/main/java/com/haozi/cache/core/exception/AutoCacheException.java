package com.haozi.cache.core.exception;

public class AutoCacheException extends RuntimeException {
    public AutoCacheException() {
    }

    public AutoCacheException(String message) {
        super(message);
    }

    public AutoCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutoCacheException(Throwable cause) {
        super(cause);
    }

    public AutoCacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static AutoCacheException nullRemoteCache() {
        return new AutoCacheException("未发现远程缓存, 删除remoteTTL属性, 或配置Redis缓存.");
    }
}

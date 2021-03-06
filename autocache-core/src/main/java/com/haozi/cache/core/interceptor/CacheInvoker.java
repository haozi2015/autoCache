package com.haozi.cache.core.interceptor;

/**
 * 执行获得数据程序接口
 *
 * @author haozi
 */
@FunctionalInterface
public interface CacheInvoker {

    Object invoke() throws ThrowableWrapper;

    class ThrowableWrapper extends RuntimeException {

        private final Throwable original;

        public ThrowableWrapper(Throwable original) {
            super(original.getMessage(), original);
            this.original = original;
        }

        public Throwable getOriginal() {
            return this.original;
        }
    }
}

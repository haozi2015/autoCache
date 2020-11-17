package com.haozi.cache.core.interceptor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 缓存上下文
 *
 * @author haozi
 */
@Data
@AllArgsConstructor
public class CacheOperationContext {
    private CacheOperation operation;
    //  class targetClass = AopProxyUtils.ultimateTargetClass(target);
    private Object target;
    private Method method;
    private Object[] args;
    private CacheInvoker invoker;

    /**
     * 返回MAP形式集合
     *
     * @return true：是；false；否
     */
    public boolean isCacheFieldMap() {
        return operation.isElementCache() && method.getReturnType().isAssignableFrom(Map.class);
    }

    /**
     * 返回List集合形式
     *
     * @return true：是；false；否
     */
    public boolean isCacheFieldList() {
        return operation.isElementCache() && method.getReturnType().isAssignableFrom(List.class);
    }

    /**
     * 第一个参数是否是集合
     * <p>
     * 影响是否可以单个元素缓存
     *
     * @return true：是；false；否
     */
    public boolean isArgsList() {
        return args != null && args.length >= 1 && args[0] instanceof List;
    }

    public List getArgs0List() {
        if (args[0] instanceof List) {
            return (List) (args[0] = new ArrayList((List) args[0]));
        }
        throw new IllegalArgumentException("arg[0] not instance of List");
    }
}

package com.haozi.cache.core.util;

import com.haozi.cache.core.AutoCache;
import com.haozi.cache.core.AutoCacheField;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 缓存工具类
 *
 * @author haozi
 * @date 2020/10/206:29 下午
 */
public class CacheUtil {
    private final static ExpressionParser parser = new SpelExpressionParser();
    private final static DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    // 默认参数间隔
    public final static String SPLIT = ":";

    /**
     * 生成缓存key
     *
     * @param keySEL key表达式，详见Spring Expression Language (SpEL)语法
     * @param method 方法
     * @param args   参数
     * @return
     */
    public static Object generateKey(String keySEL, Method method, Object[] args) {
        if (StringUtils.isEmpty(keySEL)) {
            throw new IllegalArgumentException("key is empty");
        }
        Expression expression = parser.parseExpression(keySEL);
        if (args.length == 0) {
            return expression.getValue();
        }
        EvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return expression.getValue(context);
    }

    /**
     * 生成缓存默认key
     * <p>
     * 注解未定义{@link AutoCache#key()}使用
     *
     * @param args
     * @return
     */
    public static Object generateDefaultKey(Object[] args) {
        if (args != null && args.length == 1) {
            return args[0];
        }
        return Stream.of(args)
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    /**
     * 获取对象属性声明{@AutoCacheField}注解的值
     *
     * @param obj
     * @return
     */
    public static Object getCacheFieldValue(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                fields[i].setAccessible(true);
                AutoCacheField autoCacheField = fields[i].getAnnotation(AutoCacheField.class);
                if (autoCacheField == null) {
                    continue;
                }
                return fields[i].get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取默认缓存名称
     *
     * @param method
     * @return
     */
    public static String getDefaultCacheName(Method method) {
        StringBuilder nameBuilder = new StringBuilder(method.getClass().getCanonicalName());
        nameBuilder.append(CacheUtil.SPLIT).append(method.getName());
        return nameBuilder.toString();
    }
}

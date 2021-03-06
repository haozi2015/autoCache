# autoCache

#### 概述
`autoCache`是一个Java轻量级的二级缓存组件。首先，autoCache 本身并无缓存功能，而是利用内存缓存`caffeine`和高性能数据库`Redis`，实现的二级缓存组件。
`autoCache` 解决高并发场景时缓存逻辑复杂的问题，以极简的方式实现缓存功能，提升系统性能、开发效率。

#### 基本要求

1.  JDK 1.8+
2.  Spring boot 2.0+
3.  添加依赖
```xml
<dependency>
  <groupId>io.github.haozi2015</groupId>
  <artifactId>autocache-spring-boot-starter</artifactId>
  <version>1.0.4</version>
</dependency>
```

#### 使用说明
方法使用`@AutoCache`注解，即完成方法的参数和结果缓存。

```java
    // 仅用本地缓存，5秒后过期
    @AutoCache(localTTL = 5)
    String getLocalStr() {
        return "abc";
    }

    // 仅用远程缓存
    @AutoCache(remoteTTL = 30)
    String getRemoteStr() {
        return "abc";
    }
            
    // 二级缓存
    // 优先本地缓存，不存在时查远程缓存，不存在是调用原方法，结果再依次被远程和本地缓存
    // 分别本地缓存5秒过期，远程缓存30秒过期
    @AutoCache(localTTL = 5, remoteTTL = 30)
    String getStr() {
        return "abc";
    }

```
远程缓存，配置Redis同SpringBoot方式。

**更多功能**

+ [限制内存缓存最大条数](./autocache-spring-boot-starter-demo/src/main/java/com/haozi/cache/demo/SimpleAutoCacheDemo.java)
+ [自定义缓存KEY](./autocache-spring-boot-starter-demo/src/main/java/com/haozi/cache/demo/HighLevelAutoCacheDemo.java)
+ [清除缓存](./autocache-spring-boot-starter-demo/src/main/java/com/haozi/cache/demo/HighLevelAutoCacheDemo.java)
+ [集合，按元素颗粒度缓存](./autocache-spring-boot-starter-demo/src/main/java/com/haozi/cache/demo/ComplexAutoCacheDemo.java)

完整代码，参考[demo](./autocache-spring-boot-starter-demo/src/main/java/com/haozi/cache/demo)

#### TODO 
1. 主动缓存功能
2. 监控，访问量、命中率、miss率等
3. 内存缓存的一致性问题


 

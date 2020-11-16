# autoCache

#### 介绍
`autoCache`是一个Java轻量级的二级缓存组件，对业务零侵入，可大幅提升系统性能、开发效率和代码整洁。

#### 软件架构
`autoCache`采用二级缓存的设计，一级缓存（L1）为内存缓存，`内存`容量小，性能高; 二级缓存（L2）为远程缓存，又称集中缓存，相对`内存`速度稍低，但容量更大、扩展性更强、更易于维护。

`autoCache`的L1使用当下流行的caffeine，L2集中缓存使用Redis，可继续扩展三方缓存组件。

`autoCache`简化**接入**设计，基于Spring boot体系，实现引入即用，摒弃传统手动注入Bean，配置AOP切面的方式。

`autoCache`简化**使用**设计，基于注解方式，实现对业务零侵入，摒弃代码声明侵入方式，与业务耦合。

#### 基本要求

1.  JDK 1.8+
2.  Spring boot 2.0+
3.  Redis
4.  添加maven依赖（中央仓库上传中）
```xml
<dependency>
    <groupId>com.haozi</groupId>
    <artifactId>spring-boot-starter-autocache</artifactId>
    <version>last version</version>
</dependency>
```

#### 使用说明
`@AutoCache` 自动缓存注解，二级缓存优先级：本地缓存 > 远程缓存 > 回源

```java

    // 本地缓存，5秒过期
    @AutoCache(localTTL = 5)
    public Teacher getTeacher(Long id) {
        return new Teacher();
    }

    // 远程缓存，30秒过期
    @AutoCache(remoteTTL = 30)
    public Teacher getTeacher2(Long id) {
        return new Teacher();
    }

    // 二级缓存，本地5秒，远程30秒
    @AutoCache(localTTL = 5, remoteTTL = 30)
    public Teacher getTeacher3(Long id) {
        return new Teacher();
    }

```
`@AutoCache` 自动缓存注解，key支持Spring Expression Language (SpEL)解析表达式
```java
    @AutoCache(localTTL = 5, key = "#id+'-'+#name")
    public Teacher getTeacher(Long id,String name) {
        return new Teacher();
    }

```
`@AutoCache`注解`elementCache`为true时，支持集合元素的缓存，并回源miss部分的元素

```java
    // 缓存集合元素，List方式
    @AutoCache(localTTL = 5, elementCache = true)
    public List<Teacher> getTeacher(List<Long> ids) {
        return Arrays.asList(new Teacher());
    }
    // 缓存集合元素，Map方式
    @AutoCache(localTTL = 5, elementCache = true)
    public Map<Long,Teacher> getTeacher2(List<Long> ids) {
        return new HashMap<>();
    }

```

`@AutoCacheEvict`自动清除缓存注解，清除缓存需声明`cacheName`缓存名称

```java
    @AutoCacheEvict(cacheName = "teacher", key = "#teacher.id")
    public Integer update(Teacher teacher) {
        return 1;
    }

```


完整代码，参考demo。

#### TODO 
1. 主动加载缓存功能
2. 监控，访问量、命中率、miss率等
3. 内存缓存，单节点清理后，其它节点的一致性问题
4. 发现，发现系统哪些方法访问量高，耗时较长


 
# autoCache

#### 介绍
`autoCache`是一个Java轻量级的二级缓存组件，对业务零侵入的方式，大幅提升开发效率和代码整洁。

`autoCache`解决常规业务定制化的缓存代码，重复率高、代码量大、出错不易排查等问题。

#### 软件架构
`autoCache`二级缓存设计，基于Spring体系，本地缓存caffeine，远程缓存Redis，可继续扩展三方缓存组件。


#### 基本要求

1.  JDK 1.8+
2.  Spring boot 2.0+
3.  添加maven依赖（中央仓库上传中）
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
`@AutoCache` 自定缓存注解，自定义key，支持Spring Expression Language (SpEL)解析表达式
```java
    @AutoCache(localTTL = 5, key = "#id+'-'+#name")
    public Teacher getTeacher(Long id,String name) {
        return new Teacher();
    }

```
`@AutoCache`自动缓存注解，`elementCache`为true时，支持集合元素的缓存，并回源miss部分的元素

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


更多功能，参考demo代码。

#### 支持
 
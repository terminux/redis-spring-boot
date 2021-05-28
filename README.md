# redis-spring-boot

### 一、基本配置

##### 1、先把项目clone到本地

> `git clone git@github.com:terminux/redis-spring-boot.git`

##### 2、进入到 `redis-spring-boot-starter` 目录下执行

> `mvn -DskipTests clean install`

##### 3、给项目添加依赖

```xml

<dependency>
    <groupId>com.ugrong.framework</groupId>
    <artifactId>redis-spring-boot-starter</artifactId>
</dependency>
```

##### 4、在项目中配置一下redis

```yml
spring:
  redis:
    password: your_password
    host: localhost
    port: 6379
```

### 二、开始使用

##### 1、假如我们需要序列化一个学生类到redis，需要实现序列化接口，例如：

```java

@Getter
@Setter
@ToString
public class Student implements Serializable {

    private Long id;

    private String name;
}
```

##### 2、实现 [IRedisCacheType](./redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/domain/IRedisCacheType.java "IRedisCacheType") 接口，并且重写 [`getValue`] 方法，例如用枚举实现：

```java
public enum EnumStudentCacheType implements IRedisCacheType {

    /**
     * 学生信息缓存
     */
    STUDENT_CACHE("STUDENT");

    private final String value;

    EnumStudentCacheType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
```

> 这样可以方便在key很多的情况下方便我们管理key,同时根据不同的业务场景统一key的规范

##### 3、创建 [`StudentRedisRepository`]，并且继承 [AbstractSimpleRedisRepository](./redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/cache/impl/AbstractSimpleRedisRepository.java "AbstractSimpleRedisRepository") ，重写它的 [`getCacheType`] 方法，例如：

```java

@Component
public class StudentRedisRepository extends AbstractSimpleRedisRepository<Student> {

    @Override
    public IRedisCacheType getCacheType() {
        return EnumStudentCacheType.STUDENT_CACHE;
    }
}
```

##### 这样就很方便地可以在 `service` 中使用:

```java
@Autowired
private StudentRedisRepository studentRedisRepository;
```

##### 来对 `redis` 中的 `Student` 进行操作了

##### 4、简单的字符串操作可以直接使用 [IStringRedisRepository](./redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/cache/IStringRedisRepository.java "IStringRedisRepository") :

```java
@Autowired
private IStringRedisRepository stringRedisRepository;
```

##### 5、为了支持不同数据结构的存储，除了 [AbstractSimpleRedisRepository](./redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/cache/impl/AbstractSimpleRedisRepository.java "AbstractSimpleRedisRepository") 和 [IStringRedisRepository](./redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/cache/IStringRedisRepository.java "IStringRedisRepository") ，

##### 还提供了 [AbstractHashRedisRepository](./redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/cache/impl/AbstractHashRedisRepository.java "AbstractHashRedisRepository")，[AbstractListRedisRepository](./redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/cache/impl/AbstractListRedisRepository.java "AbstractListRedisRepository") ，可以自行查看和使用

### 三、锁的功能

##### 1、项目中还支持了redis分布式锁，可以在 [IRedisLockRepository](./redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/lock/IRedisLockRepository.java "IRedisLockRepository") 中查看提供的方法和详细注释

##### 2、在项目中注入 [IRedisLockRepository](./redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/lock/IRedisLockRepository.java "IRedisLockRepository") ：

```java
 @Autowired
private IRedisLockRepository redisLockRepository;
```

##### 3、执行加锁

##### 伪代码如下：

```
try {
        if (redisLockRepository.tryLock(...)) {
            //获取到锁
            //do something
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        //进行解锁
        redisLockRepository.unlock(...);
    }
```

##### 4、示例

##### 4.1、实现 [IRedisLockType](./redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/domain/IRedisLockType.java "IRedisLockType") 接口，并且重写 [`getValue`] 方法，例如用枚举实现：

```java
public enum EnumStudentLockType implements IRedisLockType {

    STUDENT_LOCK("STUDENT_LOCK");

    private final String value;

    EnumStudentLockType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
```

##### 4.2、对id为 `123456` 的学生进行加锁

```
String lockField = "123456";
try {
    if (redisLockRepository.tryLock(EnumStudentLockType.STUDENT_LOCK, lockField, 20, 20, TimeUnit.SECONDS)) {
        //获取到锁
        //do something
    }
} catch (Exception e) {
    e.printStackTrace();
} finally {
    //进行解锁
    redisLockRepository.unlock(EnumStudentLockType.STUDENT_LOCK, lockField);
}
```

##### 5、提供了 [@RedisLock](./redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/annotation/RedisLock.java "@RedisLock") 注解来支持aop加锁






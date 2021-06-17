# redis-spring-boot

> 一个轻量级用于简化redis操作，基于spring boot的starter组件，并且提供了基于redis的分布式锁

### 1.x 版本不再处于活跃的开发阶段，目前处于维护模式，请使用更高版本。

### 一、基本配置

##### 1、先把项目clone到本地

```shell
git clone git@github.com:terminux/redis-spring-boot.git
```

##### 2、到项目根路径下执行

`mvn -DskipTests clean install`

##### 3、给自己的项目添加依赖
```xml
<dependency>
  <groupId>com.ugrong.framework</groupId>
  <artifactId>redis-spring-boot-starter</artifactId>
  <version>1.0.0</version>
</dependency>
```
##### 4、在自己的项目中配置一下redis
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
##### 2、实现[RedisKeyPrefix](https://github.com/huguirong/redis-spring-boot/blob/master/redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/domain/RedisKeyPrefix.java "RedisKeyPrefix")接口，并且重写getPrefix和getDesc方法，例如用枚举实现：
```java
public enum StudentPrefix implements RedisKeyPrefix {

    STUDENT("学生key前缀");

    private final String desc;

    StudentPrefix(String desc) {
        this.desc = desc;
    }

    @Override
    public String getPrefix() {
        return this.name();
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
```
>这样可以方便在key很多的情况下方便我们管理key,同时根据不同的业务场景统一key的规范

##### 3、创建StudentRedisRepository，并且继承[AbstractRedisRepository](https://github.com/huguirong/redis-spring-boot/blob/master/redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/impl/AbstractRedisRepository.java "AbstractRedisRepository")，重写它的getKeyPrefix方法，例如：
```java
@Repository
public class StudentRedisRepository extends AbstractRedisRepository<Student>{

    @Override
    public RedisKeyPrefix getKeyPrefix() {
        return StudentPrefix.STUDENT;
    }
}
```
##### 这样就很方便地可以在service中使用:
```java
@Autowired
private StudentRedisRepository studentRedisRepository;
```
##### 来对redis中的Student进行操作了

##### 4、为了支持不同数据结构的存储，除了[AbstractRedisRepository](https://github.com/huguirong/redis-spring-boot/blob/master/redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/impl/AbstractRedisRepository.java "AbstractRedisRepository")，还提供了[AbstractHashRedisRepository](https://github.com/huguirong/redis-spring-boot/blob/master/redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/impl/AbstractHashRedisRepository.java "AbstractHashRedisRepository")，[AbstractListRedisRepository](https://github.com/huguirong/redis-spring-boot/blob/master/redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/impl/AbstractListRedisRepository.java "AbstractListRedisRepository")，[StringRedisRepositoryImpl](https://github.com/huguirong/redis-spring-boot/blob/master/redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/impl/StringRedisRepositoryImpl.java "StringRedisRepositoryImpl")，可以自行查看和扩展

### 三、锁的功能
##### 1、项目中还支持了redis分布式锁，可以在[RedisLockService](https://github.com/huguirong/redis-spring-boot/blob/master/redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/lock/service/RedisLockService.java "RedisLockService")中查看提供的方法，详细注释可以在[LockRedisRepository](https://github.com/huguirong/redis-spring-boot/blob/master/redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/LockRedisRepository.java "LockRedisRepository")中查看，具体实现可以在[LockRedisRepositoryImpl](https://github.com/huguirong/redis-spring-boot/blob/master/redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/repository/impl/LockRedisRepositoryImpl.java "LockRedisRepositoryImpl")中查看，同时可以根据自己的业务场景进行扩展

##### 2、直接在项目中使用：
```java
 @Autowired
 private RedisLockService redisLockService;
```
##### 伪代码如下：
```java
try {
          if (redisLockService.lock(...)){
                //获取到锁
                //do something
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisLockService.unlock(...);
        }
```
##### 注意：unlock时传的lockField，expectedValue要和lock时传的field，lockValue一致才能正确解锁

##### 3、可以使用tryLock或tryLockWithDefaultTimeout方法来替代lock方法以轮询的方式去获得锁。

##### 4、提供了[@RedisLock](https://github.com/huguirong/redis-spring-boot/blob/master/redis-spring-boot-autoconfigure/src/main/java/com/ugrong/framework/redis/annotation/RedisLock.java "@RedisLock")注解来支持aop加锁






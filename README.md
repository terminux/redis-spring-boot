### 一、基本配置

##### 1、先把项目clone到本地

`git clone https://github.com/huguirong/redis-spring-boot.git`

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

### 三、锁的功能






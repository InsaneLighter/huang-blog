###Huang-Blog
bug记录
```java
1. 自定义id生成异常
FUNCTION blog.nextval does not exist] with root cause
    
2.mybatis数据自动填充异常
Column 'create_time' cannot be null] with root cause
mysql中字段为datatime类型  java中对应的类型为Date类型 否则无法正确填充

3.Cannot construct instance of `java.lang.String[]`: no String-argument constructor/factory method to deserialize from String
前端转换传参格式 Array.of(ids)

4.
lettuce包异常(系统长时间不控制则抛下述异常)
io.lettuce.core.RedisCommandTimeoutException: Command timed out
jedis包异常
java.util.NoSuchElementException: Timeout waiting for idle object
```
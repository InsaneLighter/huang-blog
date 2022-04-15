###Huang-Blog
bug记录
```java
1. 自定义id生成异常
FUNCTION blog.nextval does not exist] with root cause
    
2.mybatis数据自动填充异常
Column 'create_time' cannot be null] with root cause
mysql中字段为datatime类型  java中对应的类型为Date类型 否则无法正确填充
```
# dyds-spring-boot-starter
spring-starter 注解式动态数据源新加载/切换

支持直接指定db或根据指定方法与被注解方法参数动态切换

不强制使用多数据源

使用：
1.引入依赖
        <dependency>
            <groupId>com.exfu.tool</groupId>
            <artifactId>dyds-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
2.spring application配置文件
使用dyds
dyds.open=true
配置默认数据源
dyds.def.url=jdbc:mysql://xxx/xxx
dyds.def.username=username
dyds.def.password=password
3.

# dyds-spring-boot-starter
spring-starter 注解式动态数据源新加载/切换

支持直接指定db或根据指定方法与被注解方法参数动态切换

不强制使用多数据源

## 1.引入依赖
        <dependency>
            <groupId>com.exfu.tool</groupId>
            <artifactId>dyds-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
## 2.spring application配置文件
使用dyds

dyds.open=true

配置默认数据源

dyds.def.url=jdbc:mysql://xxx/xxx

dyds.def.username=username

dyds.def.password=password

## 3.自定义配置
以上操作完成，spring容器中会存在名为dataSource的动态数据源bean
### 两种方式切换数据源
#### 1.手动切换

切换到默认数据源

com.exfu.tool.dyds.dynamicDb.DataSourceContextHolder#setDefault

切换到指定数据源

com.exfu.tool.dyds.dynamicDb.DataSourceContextHolder#set

切换到指定数据源，如失败，切换到默认数据源

com.exfu.tool.dyds.dynamicDb.DataSourceContextHolder#setOrDefault

#### 2.注解切换
    @DateSource(db = "12")
    public Object serviceMethod1() {
        return null;
    }
    
    @DateSource(target = "#{s}",dbFunction = "com.test.GetDbFunc#get")
    public Object serviceMethod2(String s) {
        return null;
    }
    dbFunction为用户自定义 根据方法参数对应查找数据源标识方法 ps:GetDbFunc类需在spriong容器内
### 动态加载新数据源
实现com.exfu.tool.dyds.inter.getDb接口，并置于容器内


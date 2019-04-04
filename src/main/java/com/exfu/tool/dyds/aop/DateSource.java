package com.exfu.tool.dyds.aop;

import com.exfu.tool.dyds.dynamicDb.DataSourceContextHolder;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title: DateSource
 * @Description: 动态切换数据源 不可加载在接口处
 * @date: 2019/3/19 0019 10:40
 * @version: V1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DateSource {

    /**
     * 方式1：直接指定需切换的数据库标识
     */
    String db() default DataSourceContextHolder.DEFAULT_LOOKUP_KEY;

    /**
     * 方式2：指定生成数据库标识的方法，由被注解方法参数传入方法生成数据库标识
     * {@link #dbFunction} 需切换的数据库标识生成方法
     * {@link #target} 数据库标识生成方法 所需被注解方法参数
     */
    String dbFunction() default DateSourcesAop.DefMethod;
    String target() default DateSourcesAop.DefMethodArg;

}

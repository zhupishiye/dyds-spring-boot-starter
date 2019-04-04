package com.exfu.tool.dyds.aop;

import com.exfu.tool.dyds.dynamicDb.DataSourceContextHolder;
import com.exfu.tool.dyds.tools.ApplicationContentUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @Title: DateSourcesAop
 * @Description:
 * @date: 2019/3/19 0019 10:57
 * @version: V1.0
 */
@Aspect
@Slf4j
public class DateSourcesAop {
    public static final String DefMethod = "com.exfu.tool.dyds.tools.GetProxy#get";
    public static final String DefMethodArg = "#{target}";

    @Autowired
    private AnnotationResolver annotationResolver;

    @Around("@annotation(com.exfu.tool.dyds.aop.DateSource)")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        DateSource dateSource = method.getAnnotation(DateSource.class);

        String db = dateSource.db();
        String suplier = dateSource.dbFunction();
        String target = (String) annotationResolver.resolver(joinPoint, dateSource.target());

        if (DataSourceContextHolder.DEFAULT_LOOKUP_KEY.equals(db) && !StringUtils.isEmpty(target)) {
            String[] split = suplier.split("#");
            Class<?> aClass = Class.forName(split[0]);
            Object bean = ApplicationContentUtils.getBean(aClass);
            Method method1 = aClass.getMethod(split[1], String.class);
            db = (String) method1.invoke(bean, target);
        }
//      log.info("切换数据源：{}", db);
        if(StringUtils.isEmpty(db)) throw new Exception("未找到数据库标识");
        DataSourceContextHolder.set(db);
        Object ret = joinPoint.proceed();
        DataSourceContextHolder.clear();
        return ret;
    }
}

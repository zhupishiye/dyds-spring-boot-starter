package com.exfu.tool.dyds.core;

import com.exfu.tool.dyds.aop.AnnotationResolver;
import com.exfu.tool.dyds.aop.DateSourcesAop;
import com.exfu.tool.dyds.dynamicDb.DynamicRoutingDataSource;
import com.exfu.tool.dyds.tools.ApplicationContentUtils;
import com.exfu.tool.dyds.tools.GetProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @Title: DydsAutoConfigure
 * @Description:
 * @date: 2019/4/3 0003 15:57
 * @version: V1.0
 */
@Configuration
@EnableConfigurationProperties(DefJdbc.class)
//@ConditionalOnBean(getDb.class) //非强制多数据源
@ConditionalOnProperty(prefix = "dyds",value = "open",havingValue = "true")
@Order(1)
public class DydsAutoConfigure {

    @Autowired
    private DefJdbc defJdbc;

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    DataSource dateSource(){
        return new DynamicRoutingDataSource(defJdbc);
    }

    @Bean
    @ConditionalOnMissingBean(PlatformTransactionManager.class)
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    DateSourcesAop dateSourcesAop(){
        return new DateSourcesAop();
    }

    @Bean
    AnnotationResolver annotationResolver(){
        return new AnnotationResolver();
    }

    @Bean
    ApplicationContentUtils dydsContentUtils(){
        return new ApplicationContentUtils();
    }

    @Bean
    GetProxy getProxy(){
        return new GetProxy();
    }
}

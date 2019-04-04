package com.exfu.tool.dyds.dynamicDb;

import com.alibaba.druid.pool.DruidDataSource;
import com.exfu.tool.dyds.core.DefJdbc;
import com.google.common.collect.Lists;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static com.exfu.tool.dyds.dynamicDb.DataSourceContextHolder.DEFAULT_LENIENT_FALLBACK;
import static com.exfu.tool.dyds.dynamicDb.DataSourceContextHolder.DEFAULT_LOOKUP_KEY;

/**
 * @Title: DynamicRoutingDataSource
 * @Description:
 * @date: 2019/3/15 0015 18:32
 * @version: V1.0
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private DefJdbc defJdbc;

    @PostConstruct
    public void init(){
        this.setTargetDataSources(new HashMap<>());
    }

    @EventListener(value = ApplicationReadyEvent.class)
    public void initDateSources() throws SQLException {
        addTargetDataSource(DEFAULT_LOOKUP_KEY,
                DynamicRoutingDataSource.createDataSource(defJdbc));
    }

    public DynamicRoutingDataSource(DefJdbc defJdbc) {
        this.defJdbc = defJdbc;
    }

    /**
     * 数据源池
     */
    private static Map<Object, Object> outTargetDataSources = new HashMap<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return Optional.ofNullable(DataSourceContextHolder.getLookupKey()).orElse(DEFAULT_LOOKUP_KEY);
    }

    @Override
    public void setLenientFallback(boolean lenientFallback) {
        super.setLenientFallback(Optional.ofNullable(DataSourceContextHolder.getPerferDefault()).orElse(DEFAULT_LENIENT_FALLBACK));
    }

    /**
     * 设置数据源池
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        outTargetDataSources = targetDataSources;
        super.setTargetDataSources(targetDataSources);
        afterPropertiesSet();
    }

    /**
     * 新增数据源
     */
    public void addTargetDataSource(Object key, DataSource dataSource) {
        outTargetDataSources.put(key, dataSource);
        setTargetDataSources(outTargetDataSources);
    }

    /**
     * 数据源是否存在
     */
    public static boolean isExist(Object key) {
        return outTargetDataSources.containsKey(key) && outTargetDataSources.get(key) != null;
    }


    /**
     * 创建数据源
     */
    public static DataSource createDataSource(String driverClassName, String url, String username, String password) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        initDataSourcesConfig(dataSource);

        return dataSource;
    }

    private static void initDataSourcesConfig(DruidDataSource dataSource) throws SQLException {
        dataSource.setConnectionInitSqls(Lists.newArrayList("set names utf8mb4;"));
        dataSource.setFilters("mergeStat");
        dataSource.setMaxActive(20);
        dataSource.setInitialSize(10);
        dataSource.setMaxWait(60000);
        dataSource.setMinIdle(5);
        dataSource.setTimeBetweenEvictionRunsMillis(600000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        Properties properties = new Properties();
        properties.setProperty("druid.stat.slowSqlMillis", "200");
        dataSource.setConnectProperties(properties);
        dataSource.setPoolPreparedStatements(false);
        dataSource.setRemoveAbandoned(false);
        dataSource.setUseGlobalDataSourceStat(false);
    }

    /**
     * 创建数据源
     */
    public static DataSource createDataSource(DataSourceInfo dataSourceInfo) throws SQLException {
        return createDataSource(
                dataSourceInfo.getDriverClassName(),
                dataSourceInfo.getUrl(),
                dataSourceInfo.getUsername(),
                dataSourceInfo.getPassword());
    }

}

package com.exfu.tool.dyds.dynamicDb;

import com.exfu.tool.dyds.inter.getDb;
import com.exfu.tool.dyds.tools.ApplicationContentUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * @Title: DataSourceContextHolder
 * @Description:
 * @date: 2019/3/18 0018 10:15
 * @version: V1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class DataSourceContextHolder {

    /**
     * 默认数据源
     */
    public static final String DEFAULT_LOOKUP_KEY = "default";
    /**
     * 默认是否切换默认数据源
     */
    public static final boolean DEFAULT_LENIENT_FALLBACK = false;

    /**
     * 当前数据源标识缓存
     */
    private static final ThreadLocal<String> CONTEXT_LOOKUPKEY_HOLDER = new ThreadLocal<>();
    /**
     * 未找到指定数据源 是否切换至默认数据源
     */
    private static final ThreadLocal<Boolean> CONTEXT_LENIENTFALLBACK_HOLDER = new ThreadLocal<>();

    /**
     * 手动确认切换至默认数据源
     */
    public static void setDefault() {
        CONTEXT_LOOKUPKEY_HOLDER.set(DataSourceContextHolder.DEFAULT_LOOKUP_KEY);
        CONTEXT_LENIENTFALLBACK_HOLDER.set(false);
    }

    /**
     * 设置数据源类目
     * 未找到指定数据源时 默认不切换到默认数据源，如需切换{@link #setOrDefault(String)()}
     */
    public static void set(String lookupKey) throws Exception {
        reloadLookupKey(lookupKey);
        CONTEXT_LOOKUPKEY_HOLDER.set(lookupKey);
        CONTEXT_LENIENTFALLBACK_HOLDER.set(false);
    }

    /**
     * 设置数据源类目
     */
    public static void setOrDefault(String lookupKey) throws Exception {
        reloadLookupKey(lookupKey);
        CONTEXT_LOOKUPKEY_HOLDER.set(lookupKey);
        CONTEXT_LENIENTFALLBACK_HOLDER.set(true);
    }

    private static void reloadLookupKey(String lookupKey) throws Exception {
        log.info("change db:{}",lookupKey);
        if (!DynamicRoutingDataSource.isExist(lookupKey)) {
            //加载新数据源
            setDefault();
            getDb getDb = ApplicationContentUtils.getBean(getDb.class);
            DataSourceInfo dataSourceInfo = getDb.getDb(lookupKey);
            validDb(dataSourceInfo);
            DataSource dataSource = DynamicRoutingDataSource.createDataSource(dataSourceInfo);
            if (dataSource != null) {
                DynamicRoutingDataSource routingDataSource = ApplicationContentUtils.getBean(DynamicRoutingDataSource.class);
                routingDataSource.addTargetDataSource(lookupKey, dataSource);
            }
        }
    }

    private static void validDb(DataSourceInfo dataSourceInfo) throws Exception {
        if ( dataSourceInfo==null
                || StringUtils.isEmpty(dataSourceInfo.getDriverClassName())
                || StringUtils.isEmpty(dataSourceInfo.getUrl())
                || StringUtils.isEmpty(dataSourceInfo.getUsername())
                || StringUtils.isEmpty(dataSourceInfo.getPassword())
        ) {
            throw new Exception("数据库配置错误");
        }
    }

    /**
     * 取得当前数据源标识
     */
    public static String getLookupKey() {
        return CONTEXT_LOOKUPKEY_HOLDER.get();
    }

    /**
     * 取得当前数据源是否切换默认数据源标志
     */
    public static Boolean getPerferDefault() {
        return CONTEXT_LENIENTFALLBACK_HOLDER.get();
    }

    /**
     * 清除当前线程绑定数据源数据
     */
    public static void clear() {
        CONTEXT_LOOKUPKEY_HOLDER.remove();
        CONTEXT_LENIENTFALLBACK_HOLDER.remove();
    }
}

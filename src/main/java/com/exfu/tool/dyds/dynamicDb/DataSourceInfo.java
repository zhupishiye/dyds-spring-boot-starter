package com.exfu.tool.dyds.dynamicDb;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * @Title: DataSourceInfo
 * @Description:
 * @date: 2019/3/18 0018 11:12
 * @version: V1.0
 */
@Data
@Accessors(chain = true)
public class DataSourceInfo implements InitializingBean {
    /**
     * 驱动类名
     */
    protected String driverClassName;

    /**
     * 连接地址
     */
    protected String url;

    /**
     * 用户名
     */
    protected String username;

    /**
     * 密码
     */
    protected String password;

    @Override
    public void afterPropertiesSet() {
        init();
    }

    private void init() {
        if(StringUtils.isEmpty(driverClassName)) driverClassName = DatabaseDriverClass.MYSQL_DRIVER_CLASS;
    }

    public DataSourceInfo() {
        init();
    }
}

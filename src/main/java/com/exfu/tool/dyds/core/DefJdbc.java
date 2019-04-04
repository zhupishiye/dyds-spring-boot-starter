package com.exfu.tool.dyds.core;

import com.exfu.tool.dyds.dynamicDb.DataSourceInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Title: DefJdbc
 * @Description: 默认数据源配置
 * @date: 2019/3/18 0018 11:08
 * @version: V1.0
 */
@ConfigurationProperties("dyds.def")
public class DefJdbc extends DataSourceInfo {

}

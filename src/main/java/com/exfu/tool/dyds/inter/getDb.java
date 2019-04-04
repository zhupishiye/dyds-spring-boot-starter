package com.exfu.tool.dyds.inter;

import com.exfu.tool.dyds.dynamicDb.DataSourceInfo;

/**
 * @Title: getDb
 * @Description: 根据db key新加载数据源 实现接口并放入容器内即可
 * @date: 2019/4/3 0003 16:17
 * @version: V1.0
 */
public interface getDb {
    DataSourceInfo getDb(String db);
}

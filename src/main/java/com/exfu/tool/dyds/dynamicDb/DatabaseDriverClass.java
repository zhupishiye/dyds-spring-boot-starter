package com.exfu.tool.dyds.dynamicDb;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @Title: DatabaseDriverClass
 * @Description:
 * @date: 2019/3/18 0018 11:11
 * @version: V1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseDriverClass {
    /**
     * MySQL驱动类
     */
    public static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";

    /**
     * Oracle驱动类
     */
    public static final String ORACLE_DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";

    /**
     * PostgreSQL驱动类
     */
    public static final String POSTGRESQL_DRIVER_CLASS = "org.postgresql.Driver";

    /**
     * Sql Server驱动类
     */
    public static final String SQLSERVER_DRIVER_CLASS = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
}

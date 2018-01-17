package com.hanbon.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Created by ACat on 17/01/2018.
 * ACat i lele.
 */
public class SqlType {

	public static final String SQL_SERVER	= "SQL Server";
	public static final String MYSQL		= "MySQL";
	public static final String ORACLE		= "Oracle";

	/**
	 * 获取数据库类型
	 * 其返回值为 SqlType 中表数据库类型的常量或 ""
	 *
	 * @param connection
	 * @return		SqlType.SQL_SERVER 		=> Microsoft SQL Server
	 * 				SqlType.MYSQL			=> MySQL
	 * 				SqlType.ORACLE			=> Oracle
	 * 				""						=> 未知或暂未支持的数据库
	 * @throws SQLException
	 */
	public static String getSqlType(Connection connection) throws SQLException {
		return getSqlType(connection.getMetaData());
	}

	/**
	 * 获取数据库类型
	 * 其返回值从 SqlType 中取值
	 *
	 * @param metaData
	 * @return
	 * @throws SQLException
	 */
	public static String getSqlType(DatabaseMetaData metaData) throws SQLException {
		String productName = metaData.getDatabaseProductName();
		switch (productName) {
			case "MySQL":
				return SqlType.MYSQL;
			case "Microsoft SQL Server":
				return SqlType.SQL_SERVER;
			default:
				return null;
		}
	}
}

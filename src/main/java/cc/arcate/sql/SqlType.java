package cc.arcate.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Created by ACat on 17/01/2018.
 * ACat i lele.
 */
public enum  SqlType {

	SQL_SERVER("SQL Server"),
	MYSQL("MySQL"),
	ORACLE("Oracle");

	private String typeName;

	SqlType(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * 获取数据库类型
	 * 其返回值为 SqlType 中表数据库类型的常量或 ""
	 *
	 * @param connection
	 * @return		SqlType.SQL_SERVER 		=> Microsoft SQL Server
	 * 				SqlType.MYSQL			=> MySQL
	 * 				SqlType.ORACLE			=> Oracle
	 * 				null					=> 未知或暂未支持的数据库
	 */
	public static SqlType getSqlType(Connection connection) {
		try {
			return getSqlType(connection.getMetaData());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取数据库类型
	 * 其返回值从 SqlType 中取值
	 *
	 * @param metaData
	 * @return		SqlType.SQL_SERVER 		=> Microsoft SQL Server
	 * 				SqlType.MYSQL			=> MySQL
	 * 				SqlType.ORACLE			=> Oracle
	 * 				null					=> 未知或暂未支持的数据库
	 */
	public static SqlType getSqlType(DatabaseMetaData metaData) {
		String productName = null;
		try {
			productName = metaData.getDatabaseProductName();
			switch (productName) {
				case "MySQL":
					return SqlType.MYSQL;
				case "Microsoft SQL Server":
					return SqlType.SQL_SERVER;
				default:
					return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}

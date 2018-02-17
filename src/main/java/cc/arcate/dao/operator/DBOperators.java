package cc.arcate.dao.operator;

import cc.arcate.entity.SqlType;

import java.sql.Connection;

/**
 * Created by ACat on 14/02/2018.
 * ACat i lele.
 */
public class DBOperators {

	/**
	 * 根据数据库连接获取数据库操作器
	 *
	 * @param connSrc	数据库连接
	 * @return	数据库操作器
	 */
	public static DBOperator getOperator(Connection connSrc) {

		SqlType sqlType = SqlType.getSqlType(connSrc);
		if (sqlType == null) return new DefaultOperator(connSrc);

		switch (sqlType) {
			case MYSQL:
				return new MySqlOperator(connSrc);
			case ORACLE:
				return new OracleOperator(connSrc);
			case SQL_SERVER:
				return new SqlServerOperator(connSrc);
			default:
				return new DefaultOperator(connSrc);
		}
	}
}

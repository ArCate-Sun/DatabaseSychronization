package cc.arcate.util;

import cc.arcate.config.ConfigLoader;
import cc.arcate.config.DBGlobal;
import cc.arcate.sql.Column;
import cc.arcate.sql.SqlType;
import cc.arcate.sql.Table;
import cc.arcate.util.sql.maker.SqlFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * 表内数据层级的工具
 *
 * Created by ACat on 17/01/2018.
 * ACat i lele.
 */
public class Records {

	private static Log log = ConfigLoader.getLog();

	/**
	 * 将 records 当前所指向的记录插入数据表中
	 *
	 * @param statement		数据库的 Statement
	 * @param table			数据表
	 * @param records		包含记录的 ResultSet
	 * @param sqlType		数据库类型
	 * @return
	 * @throws SQLException
	 */
	private static boolean insertRecord(Statement statement, Table table, ResultSet records, SqlType sqlType) throws SQLException {
		// 保存每一记录的所有键值对
		Map<String, Object> values = new HashMap<>();
		for (Column column : table.getColumns()) {
			String key = column.getName();
			Object value = records.getObject(key);
			values.put(key, value);
		}

		// 插入同步数据表
		String sql = SqlFactory.makeInsertSQL(table, values, sqlType);
		statement.executeUpdate(sql);
		log.log(sql, Log.EXEC_SQL);				// 日志
		return true;
	}

	/**
	 * 获得数据表中所有记录
	 *
	 * @param statement		一个 Statement
	 * @param table			数据表对象
	 * @return		包含结果的 ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getRecords(Statement statement, Table table) throws SQLException {
		return getRecords(statement, table, table.getSqlType());
	}

	/**
	 * 获得数据表中所有记录
	 *
	 * @param statement		一个 Statement
	 * @param table			数据表对象
	 * @param sqlType 		数据库类型
	 * @return		包含结果的 ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getRecords(Statement statement, Table table, SqlType sqlType) throws SQLException {
		String sql = SqlFactory.makeSelectionSQL(table, sqlType);
		ResultSet resultSet = statement.executeQuery(sql);
		log.log(sql, Log.EXEC_SQL);		// 日志
		return resultSet;
	}

	/**
	 * 获得数据表中限制条件范围内的所有记录
	 *
	 * @param statement		一个 Statement
	 * @param table			数据表对象
	 * @param limit			限制范围
	 * @return		包含结果的 ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getRecords(Statement statement, Table table, String limit) throws SQLException {
		return getRecords(statement, table, limit, table.getSqlType());
	}

	/**
	 * 获得数据表中限制条件范围内的所有记录
	 *
	 * @param statement		一个 Statement
	 * @param table			数据表对象
	 * @param limit			限制范围
	 * @param sqlType 		数据库类型
	 * @return		包含结果的 ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getRecords(Statement statement, Table table, String limit, SqlType sqlType) throws SQLException {
		String sql = SqlFactory.makeSelectionSQL(table, limit, sqlType);
		ResultSet resultSet = statement.executeQuery(sql);
		log.log(sql, Log.EXEC_SQL);		// 日志
		return resultSet;
	}

	/**
	 * 通过执行指定的 SQL 语句, 获得数据表中限制条件范围内的所有记录
	 *
	 * @param statement		一个 Statement
	 * @param table			数据表对象
	 * @param sql			SQL 语句
	 * @return		包含结果的 ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getRecordsBySQL(Statement statement, Table table, String sql) throws SQLException {
		ResultSet resultSet = statement.executeQuery(sql);
		log.log(sql, Log.EXEC_SQL);		// 日志
		return resultSet;
	}

	/**
	 * 从源数据表中复制限制条件内的所有记录至同步数据表中
	 *
	 * @param source		源数据库的 Connection
	 * @param target		同步数据库的 Connection
	 * @param table				数据表对象
	 * @return		包含结果的 ResultSet
	 * @throws SQLException
	 */
	public static boolean copyRecords(Connection source, Connection target, Table table) throws SQLException {
		return copyRecords(source, target, table, null);
	}

	/**
	 * 从源数据表中复制限制条件内的所有记录至同步数据表中
	 *
	 * @param source		源数据库的 Connection
	 * @param target		同步数据库的 Connection
	 * @param table				数据表对象
	 * @param limit				限制条件
	 * @return
	 * @throws SQLException
	 */
	public static boolean copyRecords(Connection source, Connection target, Table table, String limit) throws SQLException {

		// 获取源数据库和同步数据库的数据库类型
		SqlType typeSrc = SqlType.getSqlType(source);

		// 获取源数据表中的限制条件范围内的所有记录
		Statement stmtSource = source.createStatement();
		ResultSet records = getRecords(stmtSource, table, limit, typeSrc);		// getRecords() 已包含日志

		// 拷贝
		copyRecords(target, table, records);

		// 关闭 Statement
		stmtSource.close();

		return true;
	}

	/**
	 * 从源数据表中复制限制条件内的所有记录至同步数据表中
	 *
	 * @param target		同步数据库的 Connection
	 * @param table			数据表对象
	 * @param records		包含要复制的记录的 ResultSet
	 * @return
	 * @throws SQLException
	 */
	public static boolean copyRecords(Connection target, Table table, ResultSet records) throws SQLException {

		// 获取源数据库和同步数据库的数据库类型
		SqlType typeTgt = SqlType.getSqlType(target);

		Statement stmtTarget = target.createStatement();

		// 获取源数据表中的限制条件范围内的所有记录
		while (records.next()) {
			insertRecord(stmtTarget, table, records, typeTgt);
		}

		// 关闭 Statement
		stmtTarget.close();

		return true;
	}

	/**
	 * 从源数据表中复制限制条件内的所有记录至同步数据表中
	 *
	 * @param statement		数据库的 Connection
	 * @param table			数据表对象
	 * @param records		包含要复制的记录的 ResultSet
	 * @return
	 * @throws SQLException
	 */
	public static boolean copyRecords(Statement statement, Table table, ResultSet records, SqlType sqlType) throws SQLException {

		// 获取源数据表中的限制条件范围内的所有记录
		while (records.next()) {
			insertRecord(statement, table, records, sqlType);
		}

		return true;
	}

	/**
	 *
	 * @param connection
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public static int getAllRecordCount(Connection connection, Table table) throws SQLException {
		return getAllRecordCount(connection, table, SqlType.getSqlType(connection));
	}

	/**
	 * 获得数据表的记录的总数
	 *
	 * @param connection
	 * @param table
	 * @param sqlType
	 * @return
	 * @throws SQLException
	 */
	public static int getAllRecordCount(Connection connection, Table table, SqlType sqlType) throws SQLException {

		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(SqlFactory.makeCountAllRecordsSQL(table, sqlType));

		// Get the result needed...
		resultSet.next();
		int result = resultSet.getInt(1);

		// Close...
		resultSet.close();
		statement.close();

		return result;
	}





	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Connection src = DBGlobal.getSourceConnection();
		Connection tgt = DBGlobal.getTargetConnection();
		Table table = Tables.getTableStructure(src, "111");
		for (Column column : table.getColumns()) {
			System.out.println(column.getName() + " " + column.getNullable());
		}
//		Tables.copyTableStructure(src, tgt, "111");
//		Records.copyRecords(src, tgt, table);
//		System.out.println(Records.getAllRecordCount(src, table));
	}


}

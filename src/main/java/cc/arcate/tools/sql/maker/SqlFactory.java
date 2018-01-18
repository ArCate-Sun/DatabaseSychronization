package cc.arcate.tools.sql.maker;

import cc.arcate.config.DBGlobal;
import cc.arcate.sql.Column;
import cc.arcate.sql.Table;
import cc.arcate.tools.Tables;
import cc.arcate.tools.sql.maker.impl.*;
import cc.arcate.sql.SqlType;
import com.hanbon.tools.sql.maker.impl.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL 语句生成类
 *
 * Created by ACat on 17/01/2018.
 * ACat i lele.
 */
public class SqlFactory {

	private SqlFactory() {}

	private static SqlMaker sqlForDefult = new SqlMakerForDefault();
	private static SqlMaker sqlForMySQL = new SqlMakerForMySQL();
	private static SqlMaker sqlForSQLServer = new SqlMakerForSQLServer();
	private static SqlMaker sqlForOracle = new SqlMakerForOracle();

	private static SqlMaker getSqlMaker(SqlType sqlType) {
		switch (sqlType) {
			case MYSQL:
				return sqlForMySQL;
			case SQL_SERVER:
				return sqlForSQLServer;
			case ORACLE:
				return sqlForDefult;
			default:
				return sqlForDefult;
		}
	}

	/**
	 * 组装创建该键的 sql 语句
	 * 主要用于创建数据表的 sql 语句中对于每个键的组装
	 * 其数据库类型默认为键对象中的数据库类型
	 *
	 * @param column
	 * @return	创建该键的 sql 语句
	 */
	public static String makeColumnSQL(Column column) {
		return makeColumnSQL(column, column.getSqlType());
	}

	/**
	 * 组装创建该键的 sql 语句
	 * 主要用于创建数据表的 sql 语句中对于每个键的组装
	 *
	 * @param column
	 * @param sqlType 	数据库类型
	 * @return	创建该键的 sql 语句
	 */
	public static String makeColumnSQL(Column column, SqlType sqlType) {
		return getSqlMaker(sqlType).column(column);
	}

	/**
	 * 组装创建该表的 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table 	数据表对象
	 * @return 创建该表的 sql 语句
	 */
	public static String makeCreationSQL(Table table) {
		return makeCreationSQL(table, table.getSqlType());
	}

	/**
	 * 组装创建该表的 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table		数据表对象
	 * @param sqlType 	数据库类型
	 * @return 创建该表的 sql 语句
	 */
	public static String makeCreationSQL(Table table, SqlType sqlType) {
		return getSqlMaker(sqlType).makeCreationSQL(table);
	}

	/**
	 * 组装查询数据表的 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table		数据表对象
	 * @return	查询数据表的 sql 语句
	 */
	public static String makeSelectionSQL(Table table) {
		return makeSelectionSQL(table, null, null, null, table.getSqlType());
	}

	/**
	 * 组装查询数据表的 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table		数据表对象
	 * @param sqlType 	数据库类型
	 * @return	查询数据表的 sql 语句
	 */
	public static String makeSelectionSQL(Table table, SqlType sqlType) {
		return makeSelectionSQL(table, null, null, null, sqlType);
	}

	/**
	 * 组装查询数据表的 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table		数据表对象
	 * @param limit		限制条件语句
	 * @return	查询数据表的 sql 语句
	 */
	public static String makeSelectionSQL(Table table, String limit) {
		return makeSelectionSQL(table, null, limit, null, table.getSqlType());
	}

	/**
	 * 组装查询数据表的 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table		数据表对象
	 * @param limit		限制条件语句
	 * @param sqlType 	数据库类型
	 * @return	查询数据表的 sql 语句
	 */
	public static String makeSelectionSQL(Table table, String limit, SqlType sqlType) {
		return makeSelectionSQL(table, null, limit, null, sqlType);
	}

	/**
	 * 组装查询数据表的 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table		数据表对象
	 * @param columns	查询的键名
	 * @param limit		限制条件语句
	 * @return	查询数据表的 sql 语句
	 */
	public static String makeSelectionSQL(Table table, String columns, String limit) {
		return makeSelectionSQL(table, columns, limit, null, table.getSqlType());
	}

	/**
	 * 组装查询数据表的 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table		数据表对象
	 * @param columns	查询的键名
	 * @param limit		限制条件语句
	 * @param sqlType 	数据库类型
	 * @return	查询数据表的 sql 语句
	 */
	public static String makeSelectionSQL(Table table, String columns, String limit, SqlType sqlType) {
		return makeSelectionSQL(table, columns, limit, null, sqlType);
	}

	/**
	 * 组装查询数据表的 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table		数据表对象
	 * @param columns	查询的键名
	 * @param limit		限制条件语句
	 * @param index		优化索引语句
	 * @return	查询数据表的 sql 语句
	 */
	public static String makeSelectionSQL(Table table, String columns, String limit, String index) {
		return makeSelectionSQL(table, columns, limit, index, table.getSqlType());
	}

	/**
	 * 组装查询数据表的 sql 语句
	 *
	 * @param table		数据表对象
	 * @param columns	查询的键名
	 * @param limit		限制条件语句
	 * @param index		优化索引语句
	 * @param sqlType    数据库类型
	 * @return	查询数据表的 sql 语句
	 */
	public static String makeSelectionSQL(Table table, String columns, String limit, String index, SqlType sqlType) {
		return getSqlMaker(sqlType).makeSelectionSQL(table, columns, limit, index);
	}

	/**
	 * 组装更新 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table		数据表对象
	 * @param values	更新的键值对
	 * @param where		条件语句
	 * @return	更新 sql 语句
	 */
	public static String makeUpdateSQL(Table table, Map<String, Object> values, String where) {
		return makeUpdateSQL(table,values, where, table.getSqlType());
	}

	/**
	 * 组装更新 sql 语句
	 *
	 * @param table		数据表对象
	 * @param values	更新的键值对
	 * @param where		条件语句
	 * @param sqlType	数据库类型
	 * @return	更新 sql 语句
	 */
	public static String makeUpdateSQL(Table table, Map<String, Object> values, String where, SqlType sqlType) {
		return getSqlMaker(sqlType).makeUpdateSQL(table, values, where);
	}

	/**
	 * 组装插入 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table		数据表对象
	 * @param values	插入的键值对
	 * @return	插入 sql 语句
	 */
	public static String makeInsertSQL(Table table, Map<String, Object> values) {
		return makeInsertSQL(table, values, table.getSqlType());
	}

	/**
	 * 组装插入 sql 语句
	 *
	 * @param table		数据表对象
	 * @param values	插入的键值对
	 * @param sqlType	数据库类型
	 * @return	插入 sql 语句
	 */
	public static String makeInsertSQL(Table table, Map<String, Object> values, SqlType sqlType) {
		return getSqlMaker(sqlType).makeInsertSQL(table, values);
	}

	/**
	 * 组装删除 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table		数据表对象
	 * @param where		条件语句
	 * @return	更新 sql 语句
	 */
	public static String makeDeleteSQL(Table table, String where) {
		return makeDeleteSQL(table, where, table.getSqlType());
	}

	/**
	 * 组装删除 sql 语句
	 *
	 * @param table		数据表对象
	 * @param where		条件语句
	 * @param sqlType	数据库类型
	 * @return	更新 sql 语句
	 */
	public static String makeDeleteSQL(Table table, String where, SqlType sqlType) {
		return getSqlMaker(sqlType).makeDeleteSQL(table, where);
	}

	/**
	 * 组装清空数据表 sql 语句
	 * 其数据库类型默认为 table 中的数据库类型
	 *
	 * @param table		数据表对象
	 * @return	清空 sql 语句
	 */
	public static String makeClearSQL(Table table) {
		return makeClearSQL(table, table.getSqlType());
	}


	/**
	 * 组装清空数据表 sql 语句
	 *
	 * @param table		数据表对象
	 * @param sqlType	数据库类型
	 * @return	清空 sql 语句
	 */
	public static String makeClearSQL(Table table, SqlType sqlType) {
		return getSqlMaker(sqlType).makeClearSQL(table);
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Table table = Tables.getTableStructure(DBGlobal.getSourceConnection(), "B_BILLBAD");

		Map<String, Object> values = new HashMap<>();
		values.put("id", 123);
		System.out.println(SqlFactory.makeUpdateSQL(table, values, "id=1"));
		System.out.println(SqlFactory.makeInsertSQL(table, values));

	}
}

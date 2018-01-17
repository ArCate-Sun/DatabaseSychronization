package com.hanbon.tools;

import com.hanbon.config.DBGlobal;
import com.hanbon.sql.Column;
import com.hanbon.sql.SqlType;
import com.hanbon.sql.Table;

import java.sql.SQLException;

/**
 * SQL 语句生成类
 *
 * Created by ACat on 17/01/2018.
 * ACat i lele.
 */
public class SqlMaker {




	private SqlMaker() {}

	/**
	 * 组装创建该键的 sql 语句
	 * 适用于 MySQL
	 *
	 * @param column
	 * @return	创建该键的 sql 语句
	 */
	private static String makeColumnCreationSQLForMySQL(Column column) {
		StringBuilder sql = new StringBuilder();

		// 键名
		sql.append(column.getName()).append(" ");

		// 类型
		switch (column.getTypeName().toLowerCase()) {
			case "datetime":
				sql.append(column.getTypeName()).append(" ");
				break;
			default:
				sql.append(column.getTypeName()).append("(").append(column.getSize()).append(") ");
		}

		// 主键和自增
		if (column.isPrimaryKey()) {
			sql.append("primary key ");
		}
		if (column.isAutoIncrement()) {
			sql.append("auto_increment ");
		}

		return sql.toString();
	}

	/**
	 * 组装创建该键的 sql 语句
	 * 适用于 默认情况下
	 *
	 * @param column
	 * @return	创建该键的 sql 语句
	 */
	private static String makeColumnCreationSQLForDefault(Column column) {
		StringBuilder sql = new StringBuilder();

		// 键名
		sql.append(column.getName()).append(" ");

		// 类型
		switch (column.getTypeName().toLowerCase()) {
			case "datetime":
				sql.append(column.getTypeName()).append(" ");
				break;
			default:
				sql.append(column.getTypeName()).append("(").append(column.getSize()).append(") ");
		}

		return sql.toString();
	}

	/**
	 * 组装创建该键的 sql 语句
	 *
	 * @param column
	 * @return	创建该键的 sql 语句
	 */
	public static String makeColumnCreationSQL(Column column) {
		switch (column.getSqlType()) {
			case SqlType.MYSQL:
				return makeColumnCreationSQLForMySQL(column);
			default:
				return makeColumnCreationSQLForDefault(column);
		}
	}

	/**
	 * 组装创建该表的 sql 语句
	 * 适用于 MySql
	 *
	 * @param table
	 * @return 创建该表的 sql 语句
	 */
	private static String makeTableCreationSQLForMySQL(Table table) {
		StringBuilder sql = new StringBuilder();

		sql.append("create table ").append(table.getName()).append("(");
		for (Column column : table.getColumns()) {
			sql.append(makeColumnCreationSQLForMySQL(column)).append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(") engine=InnoDB default charset='utf8'");

		return sql.toString();
	}

	/**
	 * 组装创建该表的 sql 语句
	 * 适用于 默认情况下
	 *
	 * @param table
	 * @return 创建该表的 sql 语句
	 */
	private static String makeTableCreationSQLForDefault(Table table) {
		StringBuilder sql = new StringBuilder();

		sql.append("create table ").append(table.getName()).append("(");
		for (Column column : table.getColumns()) {
			sql.append(makeColumnCreationSQLForDefault(column)).append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");

		return sql.toString();
	}

	/**
	 * 组装创建该表的 sql 语句
	 *
	 * @return 创建该表的 sql 语句
	 */
	public static String makeTableCreationSQL(Table table) {
		switch (table.getSqlType()) {
			case SqlType.MYSQL:
				return makeTableCreationSQLForMySQL(table);
			default:
				return makeTableCreationSQLForDefault(table);
		}
	}

	private static String makeTableSelectionSQLForMySQL(Table table, String columns, String limit, String index) {
		return makeTableSelectionSQLForDefault(table, columns, limit, index);
	}

	private static String makeTableSelectionSQLForDefault(Table table, String columns, String limit, String index) {
		StringBuilder sql = new StringBuilder();

		if (columns == null) {
			sql.append("select * from ");
		} else {
			sql.append("select ").append(columns).append(" ");
		}
		sql.append(table.getName());

		if (limit != null) {
			sql.append(" where ").append(limit);
		}

		return sql.toString();
	}

	public static String makeTableSelectionSQL(Table table) {
		return makeTableSelectionSQL(table, null, null, null);
	}

	public static String makeTableSelectionSQL(Table table, String limit) {
		return makeTableSelectionSQL(table, null, limit);
	}

	public static String makeTableSelectionSQL(Table table, String columns, String limit) {
		switch (table.getSqlType()) {
			case SqlType.MYSQL:
				return makeTableSelectionSQLForMySQL(table, columns, limit, null);
			default:
				return makeTableSelectionSQLForDefault(table, columns, limit, null);
		}
	}

	public static String makeTableSelectionSQL(Table table, String columns, String limit, String index) {
		switch (table.getSqlType()) {
			case SqlType.MYSQL:
				return makeTableSelectionSQLForMySQL(table, columns, limit, index);
			default:
				return makeTableSelectionSQLForDefault(table, columns, limit, index);
		}
	}


	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Table table = Tables.getTableStructure(DBGlobal.getSourceConnection(), "B_BILLBAD");

		System.out.println(SqlMaker.makeTableSelectionSQL(table, "id=1"));
	}
}

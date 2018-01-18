package cc.arcate.tools.sql.maker.impl;

import cc.arcate.sql.Column;
import cc.arcate.sql.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ACat on 17/01/2018.
 * ACat i lele.
 */
public abstract class SqlMaker {

	protected static final String TABLE_NAME	= "{table_name}";
	protected static final String CONFIG		= "{config}";
	protected static final String COLUMN_NAMES	= "{column_names}";
	protected static final String KEY_VALUES	= "{key_values}";
	protected static final String WHERE			= "{where}";
	protected static final String LIMIT			= "{limit}";
	protected static final String VALUES		= "{values}";
	protected static final String COLUMNS		= "{columns}";

	protected static final String CREATE_TABLE	= "create table {table_name} ({columns}) {config}";
	protected static final String SELECT		= "select {column_names} from {table_name} {limit}";
	protected static final String UPDATE		= "update {table_name} set {key_values} {where}";
	protected static final String INSERT		= "insert into {table_name} ({columns}) values({values})";
	protected static final String DELETE		= "delete from {table_name} {where}";

	/**
	 * 组装键的 sql 语句
	 * 适用于 默认情况下
	 * 主要用于创建数据表的 sql 语句中对于每个键的组装
	 *
	 * @param column
	 * @return	创建该键的 sql 语句
	 */
	public String column(Column column) {
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

		if (column.getNullable() == Column.COLUMN_NULLABLE) {
			sql.append("null ");
		}

		return sql.toString();
	}

	protected String createTable(String tableName, List<String> columns, String config) {

		// 组装 键
		StringBuilder sqlColumns = new StringBuilder();
		if (columns != null) {
			for (String column : columns) {
				sqlColumns.append(column).append(",");
			}
			if (sqlColumns.length() > 1) {
				sqlColumns.deleteCharAt(sqlColumns.length() - 1);
			}
		}

		String sql = CREATE_TABLE;
		sql = sql.replace(TABLE_NAME, tableName).
				replace(COLUMNS, sqlColumns).
				replace(CONFIG, config).
				replace("null", "");
		return sql;
	}

	protected String select(String tableName, String columnNames, String limit, String index) {

		String sqlCols = columnNames == null || columnNames.equals("") ? "*" : columnNames;
		String sqlLimit = limit == null || limit.equals("") ? "" : "where " + limit;
		String sql = SELECT;
		sql = sql.replace(TABLE_NAME, tableName).
				replace(COLUMN_NAMES, sqlCols).
				replace(LIMIT, sqlLimit).
				replace("null", "");

		return sql;
	}

	protected String update(String tableName, Map<String, Object> kvs, String where) {

		// 组装 键值对
		StringBuilder sqlKVs = new StringBuilder();
		if (kvs != null) {
			for (String key : kvs.keySet()) {
				sqlKVs.append(key).append("=").append(kvs.get(key)).append(",");
			}
			if (sqlKVs.length() > 0) sqlKVs.deleteCharAt(sqlKVs.length() - 1);
		}

		String sqlWhere = where == null || where.equals("") ? "" : "where " + where;
		String sql = UPDATE;
		sql = sql.replace(TABLE_NAME, tableName).
				replace(KEY_VALUES, sqlKVs).
				replace(WHERE, sqlWhere).
				replace("null", "");

		return sql;
	}

	protected String insert(String tableName, Map<String, Object> kvs) {

		// 遍历键值对, 按序生成键, 值列表
		List<String> columns = new ArrayList<>();
		List<Object> values = new ArrayList<>();
		if (kvs != null) {
			for (String key : kvs.keySet()) {
				Object value = kvs.get(key);
				if (value == null || value.equals("")) continue;
				values.add(value);
				columns.add(key);
			}
		}

		// 组装 值
		StringBuilder sqlValues = new StringBuilder();
		for (Object value : values) {
			sqlValues.append("'").append(value).append("'").append(",");
		}
		if (sqlValues.length() > 0) sqlValues.deleteCharAt(sqlValues.length() - 1);

		// 组装 键名
		StringBuilder sqlColumns = new StringBuilder();
		for (String column : columns) {
			sqlColumns.append(column).append(",");
		}
		if (sqlColumns.length() > 0) sqlColumns.deleteCharAt(sqlColumns.length() - 1);

		String sql = INSERT;
		sql = sql.replace(TABLE_NAME, tableName).
				replace(VALUES, sqlValues).
				replace(COLUMNS, sqlColumns).
				replace("null", "");

		return sql;
	}

	protected String delete(String tableName, String where) {
		String sqlWhere = where == null || where.equals("") ? "" : "where " + where;
		String sql = DELETE;
		sql = sql.replace(TABLE_NAME, tableName).
				replace(WHERE, sqlWhere).
				replace("null", "");
		return sql;
	}

	/**
	 * 组装创建数据表的 sql 语句
	 * 适用于 默认情况下
	 *
	 * @param table		数据表对象
	 * @return 创建数据表的 sql 语句
	 */
	public String makeCreationSQL(Table table) {
		List<String> columns = new ArrayList<>();

		for (Column column : table.getColumns()) {
			columns.add(column(column));
		}

		return createTable(table.getName(), columns, null);
	}

	/**
	 * 组装查询数据表的 sql 语句
	 * 适用于 默认情况下
	 *
	 * @param table		数据表对象
	 * @param columns	查询的键名
	 * @param limit		限制条件语句
	 * @param index		优化索引语句
	 * @return	查询数据表的 sql 语句
	 */
	public String makeSelectionSQL(Table table, String columns, String limit, String index) {
		return select(table.getName(), columns, limit, index);
	}

	/**
	 * 组装更新 sql 语句
	 * 适用于 默认条件下
	 *
	 * @param table		数据表对象
	 * @param kvs		更新的键值对
	 * @param where		条件语句
	 * @return	更新 sql 语句
	 */
	public String makeUpdateSQL(Table table, Map<String, Object> kvs, String where) {
		return update(table.getName(), kvs, where);
	}

	/**
	 * 组装插入 sql 语句
	 *
	 * @param table		数据表对象
	 * @param kvs		插入的键值对
	 * @return	插入 sql 语句
	 */
	public String makeInsertSQL(Table table, Map<String, Object> kvs) {
		return insert(table.getName(), kvs);
	}

	/**
	 * 组装删除 sql 语句
	 *
	 * @param table		数据表对象
	 * @param where		条件语句
	 * @return	更新 sql 语句
	 */
	public String makeDeleteSQL(Table table, String where) {
		return delete(table.getName(), where);
	}

	/**
	 * 组装清空数据表 sql 语句
	 *
	 * @param table		数据表对象
	 * @return	清空 sql 语句
	 */
	public String makeClearSQL(Table table) {
		return delete(table.getName(), null);
	}
}

package cc.arcate.util.sql.maker.impl;

import cc.arcate.sql.Column;
import cc.arcate.sql.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ACat on 17/01/2018.
 * ACat i lele.
 */
public class SqlMakerForMySQL extends SqlMaker {

	protected static final String SELECT_FROM_TO = "select {column_names} from {table_name} order by {key} asc limit {from}, {to} ";

	@Override
	public String makeCreationSQL(Table table) {
		List<String> columns = new ArrayList<>();

		for (Column column : table.getColumns()) {
			columns.add(column(column));
		}
		return createTable("`" + table.getName() + "`", columns, "engine=InnoDB default charset='utf8'");
	}

	@Override
	public String makeUpdateSQL(Table table, Map<String, Object> values, String where) {
		Map<String, Object> kvs = new HashMap<>();
		for (String key : values.keySet()) {
			kvs.put("`" + key + "`", values.get(key));
		}
		return update("`" + table.getName() + "`", kvs, where);
	}

	@Override
	public String makeInsertSQL(Table table, Map<String, Object> kvs) {
		return insert("`" + table.getName() + "`", kvs);
	}

	@Override
	public String makeSelectionSQL(Table table, String columns, String limit, String index) {
		return select("`" + table.getName() + "`", columns, limit, index);
	}

	@Override
	public String makeDeleteSQL(Table table, String where) {
		return delete("`" + table + "`", where);
	}

	@Override
	public String makeClearSQL(Table table) {
		return delete("`" + table + "`", null);
	}

	@Override
	protected String selectFromTo(String tableName, String columnNames, String key, String index, int from, int to) {
		String sqlCols = columnNames == null || columnNames.equals("") ? "*" : columnNames;
		String sql = SELECT_FROM_TO;
		sql = sql.replace(TABLE_NAME, tableName).
				replace(COLUMN_NAMES, sqlCols).
				replace(KEY, key).
				replace(FROM, from + "").
				replace(TO, to + "").
				replace("null", "");

		return sql;
	}

	@Override
	public String makeSelectionFromToSQL(Table table, String columns, String key, String index, int from, int to) {
		return this.selectFromTo("`" + table.getName() + "`", columns, key, index, from, to);
	}
}

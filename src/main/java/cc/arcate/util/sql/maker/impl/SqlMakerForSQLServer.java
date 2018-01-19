package cc.arcate.util.sql.maker.impl;

import cc.arcate.config.DBGlobal;
import cc.arcate.sql.Table;
import cc.arcate.util.Tables;

import java.sql.SQLException;

/**
 * Created by ACat on 17/01/2018.
 * ACat i lele.
 */
public class SqlMakerForSQLServer extends SqlMaker {

	protected static final String SELECT_FROM_TO = "SELECT top({count}) {column_names} from [{table_name}] where {key} not in (select top({from}) {key} from [{table_name}] order by {key} asc) ORDER BY {key} asc";

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
	@Override
	public String makeSelectionSQL(Table table, String columns, String limit, String index) {
		return select("[" + table.getName() + "]", columns, limit, index);
	}

	@Override
	protected String selectFromTo(String tableName, String column, String key, String index, int from, int to) {
		String sqlCols = column == null || column.equals("") ? "*" : column;
		String sql = SELECT_FROM_TO;
		sql = sql.replace(TABLE_NAME, tableName).
				replace(COLUMN_NAMES, sqlCols).
				replace(KEY, key).
				replace(COUNT, to - from + "")
				.replace(FROM, from + "").
						replace("null", "");

		return sql;
	}

	@Override
	public String makeSelectionFromToSQL(Table table, String columns, String key, String index, int from, int to) {
		return selectFromTo(table.getName(), columns, key, index, from, to);
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Table table = Tables.getTableStructure(DBGlobal.getSourceConnection(), "B_CONST");
		SqlMakerForSQLServer maker = new SqlMakerForSQLServer();
		String sql = maker.makeSelectionFromToSQL(table, null, "CTC_NAME", null, 0, 5);
		System.out.println(sql);
	}
}

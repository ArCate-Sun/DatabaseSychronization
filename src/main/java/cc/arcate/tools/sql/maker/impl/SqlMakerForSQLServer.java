package cc.arcate.tools.sql.maker.impl;

import cc.arcate.sql.Table;

/**
 * Created by ACat on 17/01/2018.
 * ACat i lele.
 */
public class SqlMakerForSQLServer extends SqlMaker {

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

}

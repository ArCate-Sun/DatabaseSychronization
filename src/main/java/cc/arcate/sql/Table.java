package cc.arcate.sql;

import cc.arcate.tools.sql.maker.SqlFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACat on 16/01/2018.
 * ACat i lele.
 */
public class Table {

	private SqlType sqlType;			// 数据库类型, 应取 SqlType 中的值
	private String name;			// 表名称
	private List<Column> columns = new ArrayList<>();    // 所有键属性的有序排列

	public SqlType getSqlType() {
		return sqlType;
	}

	public void setSqlType(SqlType sqlType) {
		this.sqlType = sqlType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return SqlFactory.makeCreationSQL(this);
	}
}

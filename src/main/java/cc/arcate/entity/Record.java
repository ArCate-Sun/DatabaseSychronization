package cc.arcate.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ACat on 17/02/2018.
 * ACat i lele.
 */
public class Record {

	private Connection conn;
	private SqlType sqlType;
	private Table table;
	private Map<Column, Object> kvs = new HashMap<>();

	public Record(Connection connection, Table table) {
		this.conn = connection;
		this.sqlType = SqlType.getSqlType(connection);
		this.table = table;
	}
	public Record(Connection connection, Table table, ResultSet set) throws SQLException {
		this(connection, table);
		this.readResutSet(set);
	}

	/**
	 * 将 ResultSet 对象当前指向的记录转换为记录对象
	 * 该方法不会使 ResultSet 滚动
	 *
	 * @param set
	 * @throws SQLException
	 */
	public void readResutSet(ResultSet set) throws SQLException {
		List<Column> columns = table.getColumns();
		for (Column column : columns) {
			Object value = set.getObject(column.getName());
			kvs.put(column, value);
		}
	}

	/**
	 * 添加一个记录的键值对信息
	 *
	 * @param key
	 * @param value
	 */
	public void addAttribute(Column key, Object value) {
		this.kvs.put(key, value);
	}

	public Map<Column, Object> getRecord() {
		return this.kvs;
	}

	public SqlType getSqlType() {
		return this.sqlType;
	}

}

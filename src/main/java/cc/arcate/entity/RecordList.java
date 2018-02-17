package cc.arcate.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记录组对象
 *
 * Created by ACat on 17/02/2018.
 * ACat i lele.
 */
public class RecordList {

	private Connection conn;
	private SqlType sqlType;
	private Table table;
	private List<Map<Column, Object>> records = new ArrayList<>();

	public RecordList(Connection connection, Table table) {
		this.conn = connection;
		this.sqlType = SqlType.getSqlType(conn);
		this.table = table;
	}
	public RecordList(Connection connection, Table table, ResultSet set) throws SQLException {
		this(connection, table);
		this.readResultSet(set);
	}
	public RecordList(Connection connection,Table table, List<Record> records) {
		this(connection, table);
		this.addRecords(records);
	}

	/**
	 * 将 ResultSet 对象中的所有记录转换为记录组对象
	 * 当该方法完成时, ResultSet 对象将滚动到末尾
	 *
	 * @param set
	 * @throws SQLException
	 */
	public void readResultSet(ResultSet set) throws SQLException {
		List<Column> columns = table.getColumns();
		while (set.next()) {
			Map<Column, Object> kvs = new HashMap<>();
			for (Column column : columns) {
				Object value = set.getObject(column.getName());
				kvs.put(column, value);
			}
			records.add(kvs);
		}
	}

	/**
	 * 将 Record 列表中的记录按序添加到记录组对象中
	 *
	 * @param records
	 */
	public void addRecords(List<Record> records) {
		for (Record record : records) {
			this.records.add(record.getRecord());
		}
	}

	/**
	 * 向记录组对象添加一条记录
	 *
	 * @param record
	 */
	public void addRecord(Record record) {
		this.records.add(record.getRecord());
	}

	public List<Map<Column, Object>> getRecords() {
		return this.records;
	}

	public SqlType getSqlType() {
		return this.sqlType;
	}
}

package cc.arcate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACat on 25/01/2018.
 * ACat i lele.
 */
public class Table {
	
	private String name;
	private List<Column> columns = new ArrayList<>();

	public Table() {}

	/**
	 * 拷贝构造函数
	 *
	 * @param table
	 */
	public Table(Table table) {
		this.name = table.name;
		for (Column column : table.columns) {
			this.columns.add(new Column(column));
		}
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
}

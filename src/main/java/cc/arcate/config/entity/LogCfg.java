package cc.arcate.config.entity;

import cc.arcate.Column;
import cc.arcate.Type;
import cc.arcate.Nullable;
import cc.arcate.Table;
import cc.arcate.util.Elements;
import org.dom4j.Element;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by ACat on 25/01/2018.
 * ACat i lele.
 */
public class LogCfg {

	private static final String DEFAULT_LOG_TABLE_NAME = "LOG";

	private boolean enable;
	private boolean enableLogTable;
	private Table logTable;

	public LogCfg(Element node) {
		initTable();	// 初始化日志表
		this.enable = Elements.getBooleanFromTagSelector(node, "enable", false);
		this.enableLogTable = Elements.getBooleanFromTagSelector(node, "table enable", false);
		this.logTable.setName(Elements.getStringTextFromTagSelector(node, "table name", DEFAULT_LOG_TABLE_NAME));
	}

	/**
	 * 初始化 Log Table 对象,
	 * 为其添加必要的设置和所需的键对象.
	 * 必须在 LogCfg 对象初始化时调用该方法.
	 * Log Table 对象在 LogCfg 对象创建完成后可执行的操作只有设置 Log Table 的数据表名.
	 */
	private void initTable() {

		// id 键
		Column colId = new Column();
		colId.setName("id");
		colId.setType(Type.INT);
		colId.setAutoIncrement(true);
		colId.setPrimaryKey(true);

		// time 键
		Column colTime = new Column();
		colTime.setName("time");
		colTime.setType(Type.DATETIME);
		colTime.setNullable(Nullable.COLUMN_NO_NULLS);

		// 记录日志的三个键
		Column[] columns = new Column[3];
		for (int i = 0; i < 3; ++i) {
			columns[i] = new Column();
			columns[i].setNullable(Nullable.COLUMN_NULLABLE);
			columns[i].setType(Type.CHAR);
			columns[i].setSize(255);
		}
		columns[0].setName("type");
		columns[1].setName("task");
		columns[2].setName("log");

		this.logTable = new Table();        // 创建新的 Table 对象
		// 为 Log Table 添加需要的键
		List<Column> logColumns = this.logTable.getColumns();
		logColumns.add(colId);
		logColumns.add(colTime);
		logColumns.addAll(Arrays.asList(columns));

	}

	public boolean isEnable() {
		return enable;
	}

	public boolean isEnableLogTable() {
		return enableLogTable;
	}

	/**
	 * 获取 Log Table 对象.
	 *
	 * 由于外界不应该对 Log Table 进行任何有效修改,
	 * 所以该方法返回的是一个 Log Table 的克隆对象,
	 * 并不是 Log Table 对象本身.
	 */
	public Table getLogTable() {
		return new Table(logTable);
	}
}

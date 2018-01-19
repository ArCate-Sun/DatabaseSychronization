package cc.arcate.tools;

import cc.arcate.sql.Table;
import cc.arcate.util.Records;
import cc.arcate.util.sql.maker.SqlFactory;
import cc.arcate.config.ConfigLoader;
import cc.arcate.config.DBGlobal;
import cc.arcate.sql.SqlType;
import cc.arcate.util.Log;
import cc.arcate.util.Tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 拷贝线程
 * Created by ACat on 18/01/2018.
 * ACat i lele.
 */
public class TableCopyThread extends Thread {

	private Connection source;            // 源数据库
	private Connection target;            // 同步数据库
	private Table table;                // 复制的数据表
	private String key;                    // 按照该键值的有序排序确定本线程拷贝的记录的起始

	private int workload;                // 线程总共拷贝的条数
	private int maxWorkloadOnceCopy;    // 每次最多拷贝的条数
	private int start;                    // 确定拷贝的起始点

	private Statement stmtSource;
	private Statement stmtTarget;
	private SqlType typeSrc;
	private SqlType typeTgt;

	private String task;


	private static Log log = ConfigLoader.getLog();

	public TableCopyThread(Connection source, Connection target, Table table, String key) throws SQLException {
		this(source, target, table, key, "");
	}

	public TableCopyThread(Connection source, Connection target, Table table, String key, String taskName) throws SQLException {
		this.source = source;
		this.target = target;
		this.table = table;
		this.key = key;
		this.task = taskName;
	}

	/**
	 * 设置线程的工作范围
	 * @param start
	 * @param workload
	 * @param maxWorkloadOnceCopy
	 */
	public void setLimit(int start, int workload, int maxWorkloadOnceCopy) {
		this.start = start;
		this.workload = workload;
		this.maxWorkloadOnceCopy = maxWorkloadOnceCopy <= 0 ? Integer.MAX_VALUE : maxWorkloadOnceCopy;
	}

	/**
	 * 初始化拷贝线程, 主要作用:
	 * 检测数据库连接的合法性
	 * 检测数据表对象的合法性
	 * 若有错误, 记录到日志
	 * 获取数据库的类型
	 */
	private void init() throws SQLException {

		if (this.source == null) log.log("源数据库连接为空", Log.ERR, this.task);
		if (this.target == null) log.log("目标数据库连接为空", Log.ERR, this.task);
		if (this.table == null) log.log("数据表对象为空", Log.ERR, this.task);

		this.typeSrc = SqlType.getSqlType(source);
		this.typeTgt = SqlType.getSqlType(target);

		this.stmtSource = source.createStatement();
		this.stmtTarget = target.createStatement();
	}

	@Override
	public void run() {
		try {
			// 初始化
			this.init();

			// 记录本线程拷贝最后一个记录的位置的后一位
			int end = start + workload;
			while (start < end) {

				// 计算本次循环中, 拷贝的最后一个记录的位置的后一位
				int to = start + maxWorkloadOnceCopy;
				to = to > end ? end : to;

				// 组装从源数据库查询的 SQL 语句.
				String sql = SqlFactory.makeSelectionFromToSQL(table, null, key, null, start, to, typeSrc);

				// 在源数据库执行查询 SQL 语句, 得到结果集 ResultSet
				ResultSet records = Records.getRecordsBySQL(stmtSource, table, sql);

				// 将结果集中的数据拷贝到同步数据库中
				Records.copyRecords(stmtTarget, table, records, typeTgt);

				// 下一次循环中, 起始位置为本次循环中拷贝的最后一个记录的后一位
				start = to;

				// 关闭结果集
				records.close();
			}

			// 关闭 Statement
			this.stmtSource.close();
			this.stmtTarget.close();

		} catch (SQLException e) {
			e.printStackTrace();
			log.log("不能关闭查询结果集.", Log.ERR, this.task);
		}
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Connection source = DBGlobal.getSourceConnection();
		Connection target = DBGlobal.getTargetConnection();
		Table table = Tables.getTableStructure(source, "B_CONST");

		TableCopyThread thread = new TableCopyThread(source, target, table, "CTC_NAME");
		thread.setLimit(0, 5, 2);
		thread.start();
	}
}

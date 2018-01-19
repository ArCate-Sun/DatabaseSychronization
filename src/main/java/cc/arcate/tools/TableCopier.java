package cc.arcate.tools;

import cc.arcate.util.Records;
import cc.arcate.config.ConfigLoader;
import cc.arcate.config.DBGlobal;
import cc.arcate.sql.Table;
import cc.arcate.util.Log;
import cc.arcate.util.Tables;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * Created by ACat on 18/01/2018.
 * ACat i lele.
 */
public class TableCopier {

	private static Log log = ConfigLoader.getLog();

	private Connection source;			// 源数据库
	private Connection target;			// 同步数据库
	private String tableName;			// 复制的数据表名称
	private Table table;				// 复制的数据表对象
	private boolean isNeedCreate;		// 数据表是否需要被创建
	private String key;					// 按照该键值的有序排序确定本线程拷贝的记录的起始
	private int maxWorkloadOnceCopy;    // 子线程中每次最多拷贝的条数

	private int maxThread;				// 最大线程数

	private int recordCount;			// 数据表中记录的总数

	private String task;

	public TableCopier(Connection source, Connection target, String tableName, String key) {
		this.source = source;
		this.target = target;
		this.tableName = tableName;
		this.key = key;
	}

	public TableCopier(Connection source, Connection target, Table table, String key) {
		this.source = source;
		this.target = target;
		this.table = table;
		this.key = key;
	}

	public TableCopier(Connection source, Connection target, String tableName, String key, String task) {
		this.source = source;
		this.target = target;
		this.tableName = tableName;
		this.key = key;
		this.task = task;
	}

	public TableCopier(Connection source, Connection target, Table table, String key, String task) {
		this.source = source;
		this.target = target;
		this.table = table;
		this.key = key;
		this.task = task;
	}

	public void setLimit(int maxThread, int maxWorkloadOnceCopy) {
		this.maxThread = maxThread <= 0 ? 1 : maxThread;
		this.maxWorkloadOnceCopy = maxWorkloadOnceCopy <= 0 ? Integer.MAX_VALUE : maxWorkloadOnceCopy;
	}

	public void setNeedCreate(boolean needCreate) {
		isNeedCreate = needCreate;
	}

	/**
	 * 拷贝整个表格
	 * @return
	 */
	public boolean copyTable() {
		this.init();

		if (isNeedCreate) {
			// 若需要, 在同步数据库中创建数据表
			Tables.createTable(target, table);
		}

		// 求子线程的工作量 (即总共拷贝的记录条数)
		int thdWorkload;
		if (this.recordCount % this.maxThread == 0) {
			thdWorkload = this.recordCount / this.maxThread;
		} else {
			thdWorkload = this.recordCount / this.maxThread + 1;
		}

		// 创建线程池
		ExecutorService pool = Executors.newFixedThreadPool(this.maxThread);
		for (int i = 0; i < this.maxThread; ++i) {
			// 生成子线程的任务名
			String task = this.task + "-" + i;
			// 计算子线程拷贝的起始点
			int start = thdWorkload * i;

			try {
				TableCopyThread thread = new TableCopyThread(this.source, this.target, this.table, this.key, task);
				thread.setLimit(start, thdWorkload, this.maxWorkloadOnceCopy);
				pool.submit(thread);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 主线程等待线程池执行完毕继续执行
		pool.shutdown();
		while (!pool.isTerminated());

		System.out.println("完成");

		return false;
	}

	/**
	 * 初始化,
	 * 得到数据表中记录的总数 recordCount
	 */
	private void init() {

		if (this.source == null) log.log("源数据库连接为空", Log.ERR, this.task);
		if (this.target == null) log.log("目标数据库连接为空", Log.ERR, this.task);

		try {
			if (table == null) {
				table = Tables.getTableStructure(source, tableName);
			}
			recordCount = Records.getAllRecordCount(source, table);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		Connection source = DBGlobal.getSourceConnection();
		Connection target = DBGlobal.getTargetConnection();
		Table table = Tables.getTableStructure(source, "111");
		TableCopier copier = new TableCopier(source, target, table, "STAFF_PASS");
		copier.setNeedCreate(false);
		copier.setLimit(10, 10);
		copier.copyTable();
	}
}

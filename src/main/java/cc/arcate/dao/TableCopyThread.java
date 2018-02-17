package cc.arcate.dao;

import cc.arcate.config.entity.TableCfg;
import cc.arcate.dao.operator.DBOperator;
import cc.arcate.dao.operator.DBOperators;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 分段拷贝线程
 * 每一个数据表的拷贝任务对应一个线程
 * 该线程会根据设置中数据表的最大拷贝线程数, 创建多个子拷贝线程, 将拷贝任务平均分配给每个子拷贝线程
 * 待由该线程创建的所有子拷贝线程结束后, 该分段拷贝线程结束
 *
 * Created by ACat on 14/02/2018.
 * ACat i lele.
 */
class TableCopyThread extends TableSyncThread {

	TableCopyThread(Connection connSrc, Connection connDest, TableCfg tableCfg) {
		super(connSrc, connDest, tableCfg);
	}

	@Override
	public void run() {

		DBOperator operator = DBOperators.getOperator(connSrc);

		int maxCopyThread = tableCfg.getMaxCopyThread();
		int tableSize = operator.getTableSize(tableCfg.getName());

		// 计算子拷贝线程的拷贝数目
		int separateCount = tableSize / maxCopyThread;
		if (separateCount * maxCopyThread != tableSize) ++separateCount;

		// 创建线程池
		ExecutorService pool = Executors.newFixedThreadPool(maxCopyThread);
		for (int i = 0; i < maxCopyThread; ++i) {
			// 建立多个子拷贝线程
			TableCopySeparateThread thread = new TableCopySeparateThread(this.connSrc, this.connDest, this.tableCfg);

			// 设置子拷贝线程的拷贝起始
			int from = i * separateCount + 1;
			int to = i * (separateCount + 1);
			thread.setFrom(from);
			thread.setTo(to);

			// 提交子拷贝线程
			pool.submit(thread);
		}

		// 等待线程池执行完毕继续执行
		pool.shutdown();
		while (!pool.isTerminated()) {
			try {
				sleep(500);
			} catch (InterruptedException ignored) {}
		}

	}
}

package cc.arcate.dao;

import cc.arcate.config.entity.TableCfg;
import cc.arcate.dao.operator.DBOperator;
import cc.arcate.dao.operator.DBOperators;
import cc.arcate.entity.RecordList;

import java.sql.Connection;

/**
 * 子拷贝线程
 * 由分段拷贝线程创建
 * 每一个子拷贝线程对应一个数据表拷贝任务中的某一部分任务
 * 完成该拷贝被分配的拷贝任务后, 该线程结束
 *
 * Created by ACat on 14/02/2018.
 * ACat i lele.
 */
class TableCopySeparateThread extends TableSyncThread {

	private int from;
	private int to;

	TableCopySeparateThread(Connection connSrc, Connection connDest, TableCfg tableCfg) {
		super(connSrc, connDest, tableCfg);
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	@Override
	public void run() {

		int maxCountEachCopy = tableCfg.getMaxCountEachCopy();

		DBOperator optSrc = DBOperators.getOperator(connSrc);
		DBOperator optDest = DBOperators.getOperator(connDest);

		// 每次从源表查询 maxCountEachCopy 个记录, 并写入目的表中
		int i = this.from;
		while (i < this.to) {
			RecordList recordList = optSrc.selectFromTo(this.tableCfg, i, i += maxCountEachCopy);
			optDest.insert(this.tableCfg, recordList);
		}

	}
}

package cc.arcate.dao;

import cc.arcate.config.entity.TableCfg;
import cc.arcate.dao.operator.DBOperator;
import cc.arcate.dao.operator.DBOperators;
import cc.arcate.entity.RecordList;
import cc.arcate.entity.Table;

import java.sql.Connection;

/**
 * 暴力同步模式线程
 *
 * Created by ACat on 18/02/2018.
 * ACat i lele.
 */
public class TableForceSyncThread extends TableSyncThread {

	public TableForceSyncThread(Connection connSrc, Connection connDest, TableCfg tableCfg) {
		super(connSrc, connDest, tableCfg);
	}

	@Override
	public void run() {

		DBOperator optSrc = DBOperators.getOperator(connSrc);
		DBOperator optDest = DBOperators.getOperator(connDest);

		// 若需要, 创建数据表
		if (this.tableCfg.isNeedToCreate()) {
			Table table = optSrc.getTable(tableCfg.getName());
			optDest.createTable(table);
		}

		// 从源表查询所有记录, 并写入目的表中
		RecordList recordList = optSrc.selectAll(this.tableCfg);
		optDest.insert(this.tableCfg, recordList);
	}
}

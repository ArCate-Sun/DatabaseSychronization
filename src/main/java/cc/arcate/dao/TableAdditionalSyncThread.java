package cc.arcate.dao;

import cc.arcate.config.entity.TableCfg;
import cc.arcate.dao.operator.DBOperator;
import cc.arcate.dao.operator.DBOperators;
import cc.arcate.entity.RecordList;
import cc.arcate.entity.Table;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 追加同步线程
 *
 * Created by ACat on 18/02/2018.
 * ACat i lele.
 */
public class TableAdditionalSyncThread extends TableSyncThread {

	public TableAdditionalSyncThread(Connection connSrc, Connection connDest, TableCfg tableCfg) {
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

		// TODO...

	}
}

package cc.arcate.dao;

import cc.arcate.config.entity.TableCfg;
import cc.arcate.dao.operator.DBOperator;
import cc.arcate.dao.operator.DBOperators;
import cc.arcate.entity.RecordList;

import java.sql.Connection;

/**
 * Created by ACat on 18/02/2018.
 * ACat i lele.
 */
public abstract class TableSyncThread extends Thread {

	protected Connection connSrc;
	protected Connection connDest;
	protected TableCfg tableCfg;

	public TableSyncThread(Connection connSrc, Connection connDest, TableCfg tableCfg) {
		this.connSrc = connSrc;
		this.connDest = connDest;
		this.tableCfg = tableCfg;
	}

}

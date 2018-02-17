package cc.arcate.dao.operator;

import cc.arcate.config.entity.TableCfg;
import cc.arcate.entity.Record;
import cc.arcate.entity.RecordList;
import cc.arcate.entity.Table;

import java.util.List;

/**
 * Created by ACat on 14/02/2018.
 * ACat i lele.
 */
public interface DBOperator {
	/**
	 * 根据数据表名称获取数据表的记录总数
	 *
	 * @param name	数据表名
	 * @return	数据表的记录总数
	 */
	int getTableSize(String name);

	/**
	 * 根据数据表名称获取数据表对象
	 *
	 * @param name	数据表名
	 * @return	数据表对象
	 */
	Table getTable(String name);

	/**
	 * 创建数据表
	 *
	 * @param table
	 * @return
	 */
	boolean createTable(Table table);

	/**
	 * 将记录插入数据表中
	 *
	 * @param tableCfg
	 * @param record
	 * @return 	操作是否成功
	 */
	boolean insert(TableCfg tableCfg, Record record);

	/**
	 * 将记录组数据插入数据表中
	 *
	 * @param tableCfg
	 * @param recordList
	 * @return 	操作是否成功
	 */
	boolean insert(TableCfg tableCfg, RecordList recordList);

	/**
	 * 从数据表中查询所有记录
	 *
	 * @param tableCfg
	 * @return	一个记录组对象
	 */
	RecordList selectAll(TableCfg tableCfg);

	/**
	 * 从数据表中查询第 from 到 第 to 条内的所有记录
	 *
	 * @param tableCfg
	 * @param from		查询的起始下标位置
	 * @param to		查询的终点下标位置
	 * @return	一个记录组对象
	 */
	RecordList selectFromTo(TableCfg tableCfg, long from, long to);

	/**
	 * 删除数据表中所有数据
	 *
	 * @return	操作是否成功
	 */
	boolean deleteAll(TableCfg tableCfg);

}

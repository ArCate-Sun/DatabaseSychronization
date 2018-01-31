package cc.arcate.config.entity;

import cc.arcate.config.impl.XMLConfigLoader;
import cc.arcate.util.Elements;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Objects;

/**
 * Created by ACat on 26/01/2018.
 * ACat i lele.
 */
public class TableCfg {

	private static final int DEFAULT_MAX_COPY_THREAD = 10;			// 默认最大同步线程数, 10个
	private static final int DEFAULT_MAX_COUNT_EACH_COPY = 500;		// 默认每次拷贝的记录数, 500条
	private static final int DEFAULT_SYNC_TIME_INTERVAL = 600000;	// 默认同步间隔, 10分钟
	private static final boolean DEFAULT_NEED_TO_CREATE = true;		// 默认是否需创建数据表, 需要

	private static int globalMaxCopyThread = DEFAULT_MAX_COPY_THREAD;
	private static int globalMaxCountEachCopy = DEFAULT_MAX_COUNT_EACH_COPY;
	private static int globalSyncTimeInterval = DEFAULT_SYNC_TIME_INTERVAL;
	private static boolean globalNeedToCreate = DEFAULT_NEED_TO_CREATE;

	private String name;
	private SyncType type;
	private String key;
	private String index;

	private Integer maxCopyThread;
	private Integer maxCountEachCopy;
	private Integer syncTimeInterval;
	private Boolean needToCreate;

	public TableCfg(Element node) {
		this.name = Elements.getStringTextFromTagSelector(node, "name", null);
		this.type = SyncType.getSyncType(Elements.getStringTextFromTagSelector(node, "sync-type", null));
		this.key = Elements.getStringTextFromTagSelector(node, "key", null);
		this.index = Elements.getStringTextFromTagSelector(node, "index", null);
		this.maxCopyThread = Elements.getIntFromTagSelector(node, "max-copy-thread", null);
		this.maxCountEachCopy = Elements.getIntFromTagSelector(node, "max-count-each-copy", null);
		this.syncTimeInterval = Elements.getIntFromTagSelector(node, "sync-time-interval", null);
		this.needToCreate = Elements.getBooleanFromTagSelector(node, "need-to-create", null);
	}

	/**
	 * 全局属性
	 * 获取拷贝过程中启用的最大线程数
	 *
	 * @return
	 */
	public static int getGlobalMaxCopyThread() {
		return globalMaxCopyThread;
	}

	/**
	 * 全局属性
	 * 设置拷贝过程中启用的最大线程数
	 *
	 * @param globalMaxCopyThread	其值应为一个大于 0 的数
	 *                              若为 null 和 小于 0 的值, 则设为默认值
	 */
	public static void setGlobalMaxCopyThread(Integer globalMaxCopyThread) {
		if (globalMaxCopyThread != null) {
			TableCfg.globalMaxCopyThread = globalMaxCopyThread > 0 ? globalMaxCopyThread : DEFAULT_MAX_COPY_THREAD;
		} else {
			TableCfg.globalMaxCopyThread = DEFAULT_MAX_COPY_THREAD;
		}
	}

	/**
	 * 全局属性
	 * 获取拷贝过程中每次拷贝的最大记录数
	 *
	 * @return
	 */
	public static int getGlobalMaxCountEachCopy() {
		return globalMaxCountEachCopy;
	}

	/**
	 * 全局属性
	 * 设置拷贝过程中每次拷贝的最大记录数
	 *
	 * @param globalMaxCountEachCopy	其值应为一个大于 0 的数
	 *                                  若为 null 和 小于 0 的值, 则设为默认值
	 */
	public static void setGlobalMaxCountEachCopy(Integer globalMaxCountEachCopy) {
		if (globalMaxCountEachCopy != null) {
			TableCfg.globalMaxCountEachCopy =
					globalMaxCountEachCopy > 0 ? globalMaxCountEachCopy : DEFAULT_MAX_COUNT_EACH_COPY;
		} else {
			TableCfg.globalMaxCountEachCopy = DEFAULT_MAX_COUNT_EACH_COPY;
		}
	}

	/**
	 * 全局属性
	 * 获取同步过程中每次同步的间隔时间
	 *
	 * @return
	 */
	public static int getGlobalSyncTimeInterval() {
		return globalSyncTimeInterval;
	}

	/**
	 * 全局属性
	 * 设置同步过程中每次同步的间隔时间
	 *
	 * @param globalSyncTimeInterval	其值应为一个大于 0 的数
	 *                                  若为 null 和 小于 0 的值, 则设为默认值
	 */
	public static void setGlobalSyncTimeInterval(Integer globalSyncTimeInterval) {
		if (globalSyncTimeInterval != null) {
			TableCfg.globalSyncTimeInterval =
					globalSyncTimeInterval > 0 ? globalSyncTimeInterval : DEFAULT_SYNC_TIME_INTERVAL;
		} else {
			TableCfg.globalSyncTimeInterval = DEFAULT_SYNC_TIME_INTERVAL;
		}
	}

	/**
	 * 全局属性
	 * 拷贝及同步过程之前是否需要创建数据表
	 *
	 * @return
	 */
	public static boolean isGlobalNeedToCreate() {
		return globalNeedToCreate;
	}

	/**
	 * 全局属性
	 * 设置拷贝及同步过程之前是否需要创建数据表
	 *
	 * @param globalNeedToCreate	若为 null, 则设为默认值
	 */
	public static void setGlobalNeedToCreate(Boolean globalNeedToCreate) {
		if (globalNeedToCreate != null) {
			TableCfg.globalNeedToCreate = globalNeedToCreate;
		} else {
			TableCfg.globalNeedToCreate = DEFAULT_NEED_TO_CREATE;
		}
	}

	/**
	 * 获取数据表名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取同步类型
	 * @return
	 */
	public SyncType getSyncType() {
		return type;
	}

	/**
	 * 获取拷贝及同步过程所参照的键
	 * 该键必须满足如下条件:
	 * 		插入的数据中的该键值应随插入的时间递增
	 * 		该键值是唯一的
	 *
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 获取拷贝及同步过程中强制使用的索引
	 *
	 * @return
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * 获取拷贝过程中启用的最大线程数
	 *
	 * @return
	 */
	public int getMaxCopyThread() {
		return maxCopyThread == null ? globalMaxCopyThread : maxCopyThread;
	}

	/**
	 * 获取拷贝过程中每次拷贝的最大记录数
	 *
	 * @return
	 */
	public int getMaxCountEachCopy() {
		return maxCountEachCopy == null ? globalMaxCountEachCopy : maxCountEachCopy;
	}

	/**
	 * 获取同步过程中每次同步的间隔时间
	 *
	 * @return
	 */
	public int getSyncTimeInterval() {
		return syncTimeInterval == null ? globalSyncTimeInterval : syncTimeInterval;
	}

	/**
	 * 拷贝及同步过程之前是否需要创建数据表
	 *
	 * @return
	 */
	public boolean isNeedToCreate() {
		return needToCreate == null ? globalNeedToCreate : needToCreate;
	}

}

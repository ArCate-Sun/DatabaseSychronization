package cc.arcate.config.impl;

import cc.arcate.config.entity.DatabaseCfg;
import cc.arcate.config.entity.LogCfg;
import cc.arcate.config.entity.SyncType;
import cc.arcate.config.entity.TableCfg;
import cc.arcate.config.interf.ConfigLoaderInterf;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置读取器
 * <p>
 * 给定一个配置文件, 从该文件中获得相关配置信息
 * <p>
 * Created by ACat on 15/01/2018.
 * ACat i lele.
 */
public abstract class ConfigLoader implements ConfigLoaderInterf {

	protected String configPath;                        // 配置文件路径

	protected List<DatabaseCfg> sourceCfgList = new ArrayList<>();
	protected DatabaseCfg targetCfg;
	protected List<TableCfg> tableCfgList = new ArrayList<>();
	protected LogCfg logCfg;

	protected ConfigLoader() {}

	@Override
	public String getConfigPath() {
		return configPath;
	}

	@Override
	public List<TableCfg> getAdditionalSyncTableCfgList() {
		List<TableCfg> list = new ArrayList<>();
		for (TableCfg cfg : this.tableCfgList) {
			if (cfg.getSyncType() == SyncType.ADDITIONAL_SYNC) {
				list.add(cfg);
			}
		}
		return list;
	}

	@Override
	public List<TableCfg> getForceSyncTableCfgList() {
		List<TableCfg> list = new ArrayList<>();
		for (TableCfg cfg : this.tableCfgList) {
			if (cfg.getSyncType() == SyncType.FORCE_SYNC) {
				list.add(cfg);
			}
		}
		return list;
	}

	@Override
	public List<DatabaseCfg> getSourceCfgList() {
		return this.sourceCfgList;
	}

	@Override
	public DatabaseCfg getTargetCfg() {
		return this.targetCfg;
	}



	@Override
	public LogCfg getLogCfg() {
		return this.logCfg;
	}
}

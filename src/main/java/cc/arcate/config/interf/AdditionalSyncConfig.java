package cc.arcate.config.interf;

import cc.arcate.config.entity.TableCfg;

import java.util.List;

/**
 * Created by ACat on 25/01/2018.
 * ACat i lele.
 */
public interface AdditionalSyncConfig {
	List<TableCfg> getAdditionalSyncTableCfgList();
}

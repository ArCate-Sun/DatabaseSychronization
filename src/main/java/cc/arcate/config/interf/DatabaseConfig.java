package cc.arcate.config.interf;

import cc.arcate.config.entity.DatabaseCfg;

import java.util.List;

/**
 * Created by ACat on 25/01/2018.
 * ACat i lele.
 */
public interface DatabaseConfig {
	List<DatabaseCfg> getSourceCfgList();
	DatabaseCfg getTargetCfg();
}

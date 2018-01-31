package cc.arcate.config.interf;

/**
 * Created by ACat on 25/01/2018.
 * ACat i lele.
 */
public interface ConfigLoaderInterf extends LogConfig, DatabaseConfig, AdditionalSyncConfig, ForceSyncConfig {

	/**
	 * 获取配置文件路径
	 *
	 * @return
	 */
	String getConfigPath();

}

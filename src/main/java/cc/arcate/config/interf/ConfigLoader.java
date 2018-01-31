package cc.arcate.config.interf;

/**
 * Created by ACat on 25/01/2018.
 * ACat i lele.
 */
public interface ConfigLoader extends LogConfig, DatabaseConfig, AdditionalSynchronizationConfig, ForceSynchronizationConfig {

	/**
	 * 获取配置文件路径
	 * @return
	 */
	String getConfigPath();
}

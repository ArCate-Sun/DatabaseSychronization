package cc.arcate.config.entity;

import cc.arcate.util.Elements;
import org.dom4j.Element;

/**
 * Created by ACat on 26/01/2018.
 * ACat i lele.
 */
public class DatabaseCfg {

	private static final int DEFAULT_MAX_CONNECTION_THREAD = 10;	// 默认最大数据库连接数, 10个

	private static int globalMaxConnectionThread = DEFAULT_MAX_CONNECTION_THREAD;

	private String driver;
	private String url;
	private String username;
	private String password;
	private Integer maxConnectionThread;

	public DatabaseCfg(Element node) {
		this.driver = Elements.getStringTextFromTagSelector(node, "driver", null);
		this.url = Elements.getStringTextFromTagSelector(node, "url", null);
		this.username = Elements.getStringTextFromTagSelector(node, "username", null);
		this.password = Elements.getStringTextFromTagSelector(node, "password", null);
		this.maxConnectionThread = Elements.getIntFromTagSelector(node, "max-connection-thread", null);
	}

	/**
	 * 全局设置
	 * 设置数据库最大连接线程数
	 *
	 * @param globalMaxConnectionThread		应为非负数
	 *                                      若为 null, 0 或负数, 则设置为默认值
	 */
	public static void setGlobalMaxConnectionThread(Integer globalMaxConnectionThread) {
		if (globalMaxConnectionThread != null) {
			DatabaseCfg.globalMaxConnectionThread =
					globalMaxConnectionThread > 0 ? globalMaxConnectionThread : DEFAULT_MAX_CONNECTION_THREAD;
		} else {
			DatabaseCfg.globalMaxConnectionThread = DEFAULT_MAX_CONNECTION_THREAD;
		}
	}

	/**
	 * 全局设置
	 * 获取数据库最大连接线程数
	 *
	 * @return
	 */
	public static int getGlobalMaxConnectionThread() {
		return globalMaxConnectionThread;
	}

	/**
	 * 获取 JDBC Driver
	 *
	 * @return
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * 获取数据库连接 URL
	 *
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 获取数据库连接的用户名
	 *
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 获取数据库连接的密码
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 获取数据库最大连接线程数
	 * @return
	 */
	public int getMaxConnectionThread() {
		return maxConnectionThread == null ? globalMaxConnectionThread : maxConnectionThread;
	}
}

package com.hanbon.config;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

/**
 * 配置读取器
 * <p>
 * 给定一个配置文件, 从该文件中获得相关配置信息
 * <p>
 * Created by ACat on 15/01/2018.
 * ACat i lele.
 */
public class ConfigLoader {

	// 以下是所需的配置文件中的 key
	private static final String SOURCE_DRIVER = "source.driver";
	private static final String SOURCE_URL = "source.url";
	private static final String SOURCE_USERNAME = "source.username";
	private static final String SOURCE_PASSWORD = "source.password";
	private static final String TARGET_DRIVER = "target.driver";
	private static final String TARGET_URL = "target.url";
	private static final String TARGET_USERNAME = "target.username";
	private static final String TARGET_PASSWORD = "target.password";

	private static String configPath;						// 配置文件路径
	private static Properties config = new Properties();	// 获取配置信息的对象

	static {
		loadPropertiesFromRecources("database-config.properties");
	}

	private ConfigLoader() {}

	/**
	 * 加载配置文件
	 *
	 * @param propertiesFilePath	配置文件绝对路径
	 * @return		true	读取成功
	 * 				false	读取失败
	 */
	public static boolean loadProperties(String propertiesFilePath) {
		try {
			configPath = propertiesFilePath;
			config.load(new FileInputStream(configPath));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 从 resources 中加载配置文件
	 *
	 * @param propertiesFilePath	配置文件以 resources 为起始路径的相对路径
	 * @return		true	读取成功
	 * 				false	读取失败
	 */
	public static boolean loadPropertiesFromRecources(String propertiesFilePath) {
		return loadProperties(Objects.requireNonNull(ConfigLoader.class.getClassLoader().getResource(propertiesFilePath)).getPath());
	}

	/**
	 * 获取源数据库的 JDBC Driver
	 *
	 * @return 源数据库的 JDBC Driver
	 */
	public static String getSourceJdbcDriver() {
		return config.getProperty(SOURCE_DRIVER);
	}

	/**
	 * 获取源数据库的 url
	 *
	 * @return 源数据库的 url
	 */
	public static String getSourceUrl() {
		return config.getProperty(SOURCE_URL);
	}

	/**
	 * 获取源数据库的 登录用户名
	 *
	 * @return 源数据库的 登录用户名
	 */
	public static String getSourceUsername() {
		return config.getProperty(SOURCE_USERNAME);
	}

	/**
	 * 获取源数据库的 登录密码
	 *
	 * @return 源数据库的 登录密码
	 */
	public static String getSourcePassword() {
		return config.getProperty(SOURCE_PASSWORD);
	}

	/**
	 * 获取同步数据库的 JDBC Driver
	 *
	 * @return 同步数据库的 JDBC Driver
	 */
	public static String getTargetJdbcDriver() {
		return config.getProperty(TARGET_DRIVER);
	}

	/**
	 * 获取同步数据库的 url
	 *
	 * @return 同步数据库的 url
	 */
	public static String getTargetUrl() {
		return config.getProperty(TARGET_URL);
	}

	/**
	 * 获取同步数据库的 登录用户名
	 *
	 * @return 同步数据库的 登录用户名
	 */
	public static String getTargetUsername() {
		return config.getProperty(TARGET_USERNAME);
	}

	/**
	 * 获取同步数据库的 登录密码
	 *
	 * @return 同步数据库的 登录密码
	 */
	public static String getTargetPassword() {
		return config.getProperty(TARGET_PASSWORD);
	}


	// 测试
	public static void main(String[] args) throws IOException {
		ConfigLoader.loadProperties(Objects.requireNonNull(ConfigLoader.class.getClassLoader().getResource("database-config.properties")).getPath());
	}
}

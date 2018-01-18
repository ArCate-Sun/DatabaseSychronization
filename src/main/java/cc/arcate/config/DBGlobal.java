package cc.arcate.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 在此类静态获取数据库的链接
 * Created by ACat on 16/01/2018.
 * ACat i lele.
 */
public class DBGlobal {

	// 源数据库的 JDBC Driver, Connection, Statement, url, 访问用户名及密码
	private static String driverSource;
	private static Connection connSource;
	private static Statement stmtSource;
	private static String urlSource;
	private static String unSource;
	private static String pwdSource;

	// 同步数据库的 JDBC Driver, Connection, Statement, url, 访问用户名及密码
	private static String driverTarget;
	private static Connection connTarget;
	private static Statement stmtTarget;
	private static String urlTarget;
	private static String unTarget;
	private static String pwdTarget;

	private DBGlobal() {}

	/**
	 * 获取源数据库的 JDBC Driver
	 * @return	源数据库的 JDBC Driver
	 */
	public static String getSourceJdbcDriver() {
		if (driverSource == null) driverSource = ConfigLoader.getSourceJdbcDriver();
		return driverSource;
	}

	/**
	 * 获取同步数据库的 JDBC Driver
	 * @return	同步数据库的 JDBC Driver
	 */
	public static String getTargetJdbcDriver() {
		if (driverTarget == null) driverTarget = ConfigLoader.getTargetJdbcDriver();
		return driverTarget;
	}

	/**
	 * 获取源数据库的 Connection
	 *
	 * @return 源数据库的 Connection
	 * @throws SQLException
	 */
	public static Connection getSourceConnection() throws SQLException, ClassNotFoundException {
		Class.forName(getSourceJdbcDriver());
		if (connSource == null) {
			connSource = DriverManager.getConnection(getSourceUrl(), getSourceUsername(), getSourcePassword());
		}
		return connSource;
	}

	/**
	 * 获取同步数据库的 Connection
	 *
	 * @return 同步数据库的 Connection
	 * @throws SQLException
	 */
	public static Connection getTargetConnection() throws SQLException, ClassNotFoundException {
		Class.forName(getTargetJdbcDriver());
		if (connTarget == null) {
			connTarget = DriverManager.getConnection(getTargetUrl(), getTargetUsername(), getTargetPassword());
		}
		return connTarget;
	}

	/**
	 * 获取源数据库的 Statement
	 *
	 * @return	源数据库的 Statement
	 * @throws SQLException
	 */
	public static Statement getSourceStatement() throws SQLException, ClassNotFoundException {
		if (stmtSource == null) stmtSource = getSourceConnection().createStatement();
		return stmtSource;
	}

	/**
	 * 获取目标数据库的 Statement
	 *
	 * @return	目标数据库的 Statement
	 * @throws SQLException
	 */
	public static Statement getTargetStatement() throws SQLException, ClassNotFoundException {
		if (stmtTarget == null) stmtTarget = getTargetConnection().createStatement();
		return stmtTarget;
	}

	/**
	 * 获取源数据库的 url
	 *
	 * @return 源数据库的 url
	 */
	public static String getSourceUrl() {
		if (urlSource == null) urlSource = ConfigLoader.getSourceUrl();
		return urlSource;
	}

	/**
	 * 获取同步数据库的 url
	 *
	 * @return 同步数据库的 url
	 */
	public static String getTargetUrl() {
		if (urlTarget == null) urlTarget = ConfigLoader.getTargetUrl();
		return urlTarget;
	}

	/**
	 * 获取源数据库的 登录用户名
	 *
	 * @return 源数据库的 登录用户名
	 */
	public static String getSourceUsername() {
		if (unSource == null) unSource = ConfigLoader.getSourceUsername();
		return unSource;
	}

	/**
	 * 获取同步数据库的 登录用户名
	 *
	 * @return 同步数据库的 登录用户名
	 */
	public static String getTargetUsername() {
		if (unTarget == null) unTarget = ConfigLoader.getTargetUsername();
		return unTarget;
	}

	/**
	 * 获取源数据库的 登录密码
	 *
	 * @return 源数据库的 登录密码
	 */
	public static String getSourcePassword() {
		if (pwdSource == null) pwdSource = ConfigLoader.getSourcePassword();
		return pwdSource;
	}

	/**
	 * 获取同步数据库的 登录密码
	 *
	 * @return 同步数据库的 登录密码
	 */
	public static String getTargetPassword() {
		if (pwdTarget == null) pwdTarget = ConfigLoader.getTargetPassword();
		return pwdTarget;
	}

	/**
	 * 刷新源数据库和同步数据库的 Connection, Statement, url, 访问用户名及密码
	 *
	 */
	public static void refresh() throws SQLException, ClassNotFoundException {
		// 获取连接源数据库和同步数据库所需参数
		driverSource = ConfigLoader.getSourceJdbcDriver();
		driverTarget = ConfigLoader.getTargetJdbcDriver();
		urlSource = ConfigLoader.getSourceUrl();
		urlTarget = ConfigLoader.getTargetUrl();
		unSource = ConfigLoader.getSourceUsername();
		unTarget = ConfigLoader.getTargetUsername();
		pwdSource = ConfigLoader.getSourcePassword();
		pwdTarget = ConfigLoader.getTargetPassword();

		// 加载数据库支持类
		Class.forName(driverSource);
		Class.forName(driverTarget);
		// 连接数据库
		connSource = DriverManager.getConnection(urlSource, unSource, pwdSource);
		connTarget = DriverManager.getConnection(urlTarget, unTarget, pwdTarget);
		// 创建 Statement
		stmtSource = connSource.createStatement();
		stmtTarget = connTarget.createStatement();

	}

	/**
	 * 关闭所有 Statement 及 Connection
	 *
	 */
	public static void closeConnections() {
		try {
			if (stmtSource != null) stmtSource.close();
			if (stmtTarget != null) stmtTarget.close();
			if (connSource != null) connSource.close();
			if (connTarget != null) connTarget.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		ConfigLoader.loadPropertiesFromRecources("database-config.properties");


		DBGlobal.closeConnections();
	}

}

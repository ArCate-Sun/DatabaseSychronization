package cc.arcate.tools;

import cc.arcate.sql.Column;
import cc.arcate.config.DBGlobal;
import cc.arcate.sql.SqlType;
import cc.arcate.sql.Table;
import cc.arcate.tools.sql.maker.SqlFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据表层级的工具
 *
 * Created by ACat on 16/01/2018.
 */
public class Tables {

	private static final String COLUMN_NAME = "COLUMN_NAME";			// 键名
	private static final String TYPE_NAME = "TYPE_NAME";				// 类型名
	private static final String COLUMN_SIZE = "COLUMN_SIZE";			// 键长度
	private static final String DECIMAL_DIGITS = "DECIMAL_DIGITS";		// 小数部分的位数
	private static final String NULLABLE = "NULLABLE";					// 值可以为 null
	private static final String IS_AUTOINCREMENT = "IS_AUTOINCREMENT";	// 是否自增
	private static final String REMARKS = "REMARKS";					// 键注释

	/**
	 * 根据 ResultSet 创建一个键属性对象
	 * 注意: 该方法中不调用会导致 ResultSet 结果指向改变的方法, 如 next().
	 *
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private static Column createColumnAttribute(ResultSet resultSet) throws SQLException {
		Column attr = new Column();

		attr.setName(resultSet.getString(COLUMN_NAME));
		attr.setTypeName(resultSet.getString(TYPE_NAME));
		attr.setSize(resultSet.getInt(COLUMN_SIZE));
		attr.setDecimalDigits(resultSet.getInt(DECIMAL_DIGITS));
		attr.setNullable(resultSet.getInt(NULLABLE));
		attr.setRemarks(resultSet.getString(REMARKS));

		return attr;
	}

	/**
	 * 为数据库创建数据表
	 * @param connection	数据库的 Connection
	 * @param table			数据表结构对象
	 * @return
	 */
	private static boolean createTable(Connection connection, Table table) {
		Statement stmt;
		try {
			stmt = connection.createStatement();
			return stmt.executeUpdate(SqlFactory.makeCreationSQL(table, SqlType.getSqlType(connection))) != 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取所有表格名称
	 *
	 * @param connection
	 * @return	表名的有序排列
	 * @throws SQLException
	 */
	public static List<String> getTableNames(Connection connection) throws SQLException {
		return getTableNames(connection.getMetaData());
	}

	/**
	 * 获取所有表格名称
	 *
	 * @param metaData
	 * @return	表名的有序排列
	 * @throws SQLException
	 */
	public static List<String> getTableNames(DatabaseMetaData metaData) throws SQLException {

		List<String> tableNames = new ArrayList<>();

		// 获取表名
		ResultSet resultSet = metaData.getTables(null, "%", "%", new String[]{"TABLE"});
		while (resultSet.next()) {
			tableNames.add(resultSet.getString("TABLE_NAME"));
		}
		resultSet.close();

		return tableNames;
	}

	/**
	 * 根据表名获取数据表的结构
	 *
	 * @param connection
	 * @param tableName		表名
	 * @return	表结构
	 * @throws SQLException
	 */
	public static Table getTableStructure(Connection connection, String tableName) throws SQLException {
		return getTableStructure(connection.getMetaData(), tableName);
	}

	/**
	 * 根据表名获取数据表的结构
	 *
	 * @param connection
	 * @param tableName		表名
	 * @param sqlType		数据库类型, 其值应从 SqlType 中取, 否则不生效
	 * @return	表结构
	 * @throws SQLException
	 */
	public static Table getTableStructure(Connection connection, String tableName, SqlType sqlType) throws SQLException {
		return getTableStructure(connection.getMetaData(), tableName, sqlType);
	}

	/**
	 * 根据表名获取数据表的结构
	 *
	 * @param metaData
	 * @param tableName		表名
	 * @return	表结构
	 * @throws SQLException
	 */
	public static Table getTableStructure(DatabaseMetaData metaData, String tableName) throws SQLException {
		return getTableStructure(metaData, tableName, SqlType.getSqlType(metaData));
	}

	/**
	 * 根据表名获取数据表的结构
	 *
	 * @param metaData
	 * @param tableName		表名
	 * @param sqlType		数据库类型, 其值应从 SqlType 中取, 否则不生效
	 * @return	表结构
	 * @throws SQLException
	 */
	public static Table getTableStructure(DatabaseMetaData metaData, String tableName, SqlType sqlType) throws SQLException {

		// 新建数据表结构对象, 并为其赋值表名, 数据库类型
		Table structure = new Table();
		structure.setName(tableName);
		structure.setSqlType(sqlType);

		ResultSet resultSet;

		// 获取表主键
		Set<String> primaryKeys = new HashSet<>();
		resultSet = metaData.getPrimaryKeys(null, null, tableName);
		while (resultSet.next()) {
			primaryKeys.add(resultSet.getString(COLUMN_NAME));
		}
		resultSet.close();

		// 获取表结构
		resultSet = metaData.getColumns(null, "%", tableName, "%");
		while (resultSet.next()) {
			Column attr = createColumnAttribute(resultSet);

			String colName = resultSet.getString(COLUMN_NAME);
			if (primaryKeys.contains(colName)) attr.setPrimaryKey(true);	// 判断是否为主键

			// 将键的属性添加到表结构记录中
			structure.getColumns().add(attr);
		}
		resultSet.close();

		return  structure;
	}

	/**
	 * 从源数据库中将所有的数据表结构复制到同步数据库中
	 * @param source	源数据库的 Connection
	 * @param target	同步数据库的 Connection
	 * @return			true	操作成功
	 * 					false	操作失败
	 * @throws SQLException
	 */
	public static boolean copyTableStructure (Connection source, Connection target) throws SQLException {
		List<String> names = getTableNames(source);
		return copyTableStructure(source, target, names);
	}

	/**
	 * 从源数据库中将指定表名的数据表结构复制到同步数据库中
	 * @param source	源数据库的 Connection
	 * @param target	同步数据库的 Connection
	 * @param names		要复制的表名列表
	 * @return			true	操作成功
	 * 					false	操作失败
	 * @throws SQLException
	 */
	public static boolean copyTableStructure (Connection source, Connection target, List<String> names) throws SQLException {

		for (String name : names) {
			copyTableStructure(source, target, name);
		}

		return true;
	}

	/**
	 * 从源数据库中将指定表名的数据表结构复制到同步数据库中
	 * @param source	源数据库的 Connection
	 * @param target	同步数据库的 Connection
	 * @param name		要复制的表名
	 * @return			true	操作成功
	 * 					false	操作失败
	 * @throws SQLException
	 */
	public static boolean copyTableStructure (Connection source, Connection target, String name) throws SQLException {
		Table structure = getTableStructure(source, name);
		return createTable(target, structure);
	}

	// 测试
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
//		System.out.println(DBGlobal.getSourceConnection().getMetaData().getDatabaseProductName());
		Tables.copyTableStructure(DBGlobal.getSourceConnection(), DBGlobal.getTargetConnection(), "B_BILLBAD");
	}
}

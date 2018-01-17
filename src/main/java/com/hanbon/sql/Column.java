package com.hanbon.sql;

import com.hanbon.sql.SqlType;
import com.hanbon.tools.SqlMaker;

/**
 * 键的属性
 *
 * Created by ACat on 16/01/2018.
 * ACat i lele.
 */
public class Column {

	// nullable 可取的值
	private static final int COLUMN_NO_NULLS = 0;
	private static final int COLUMN_NULLABLE = 1;
	private static final int COLUMN_NULLABLE_UNKNOWN = 2;

	private String sqlType;        		// 数据库类型, 应取 SqlType 中的值
	private String name;				// 键名
	private String typeName;			// 类型名
	private int size;					// 键长度
	private int decimalDigits;			// 小数部分的位数
	private int nullable;				// 是否允许值为 null
	private String remarks;				// 键注释
	private boolean isPrimaryKey;		// 是否为主键
	private boolean isAutoIncrement;	// 是否自增

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public int getNullable() {
		return nullable;
	}

	public void setNullable(int nullable) {
		this.nullable = nullable;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		isPrimaryKey = primaryKey;
	}

	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		isAutoIncrement = autoIncrement;
	}

	public void setAutoIncrement(String autoIncrement) {
		if (autoIncrement == null) {
			isAutoIncrement = false;
		} else {
			isAutoIncrement = autoIncrement.equals("YES");
		}
	}

	@Override
	public String toString() {
		return SqlMaker.makeColumnCreationSQL(this);
	}
}

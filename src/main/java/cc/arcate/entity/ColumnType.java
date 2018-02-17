package cc.arcate.entity;

import java.sql.*;

/**
 * Created by ACat on 25/01/2018.
 * ACat i lele.
 */
public enum ColumnType {

	CHAR(String.class, true), CHARACTER(String.class, true),
	VARCHAR(String.class, true), CHARACTERVERYING(String.class, true),
	CLOB(Clob.class),
	BLOB(Blob.class),
	INT(Integer.class), INTEGER(Integer.class),
	SMALLINT(Short.class),
	BIGINT(Long.class),
	MUMERIC(Double.class, true, 2), DECIMAL(Double.class, true, 2), DEC(Double.class, true, 2),
	REAL(Double.class),
	DOUBLE_PRECISION(Double.class),
	FLOAT(Double.class, true),
	BOOLEAN(Boolean.class),
	DATE(Date.class),
	TIME(Time.class),
	TIMESTAMP(Timestamp.class),
	INTERVAL(Long.class),

	DATETIME(java.util.Date.class);

	private Class cls;
	private boolean useParam;
	private int paramCount;

	ColumnType(Class cls) {
		this.cls = cls;
	}
	ColumnType(Class cls, boolean useSize) {
		this.cls = cls;
		this.useParam = useSize;
		this.paramCount = 1;
	}
	ColumnType(Class cls, boolean useParam, int paramCount) {
		this.cls = cls;
		this.useParam = useParam;
		this.paramCount = paramCount;
	}

	public Class getValueClassType() {
		return this.cls;
	}

	public boolean isUseParam() {
		return useParam;
	}

	public int getParamCount() {
		return paramCount;
	}
}

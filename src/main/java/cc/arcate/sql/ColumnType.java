package cc.arcate.sql;

/**
 * Created by ACat on 25/01/2018.
 * ACat i lele.
 */
public enum ColumnType {

	CHAR(true), CHARACTER(true),
	VARCHAR(true), CHARACTERVERYING(true),
	CLOB,
	BLOB,
	INT, INTEGER,
	SMALLINT,
	BIGINT,
	MUMERIC(true, 2), DECIMAL(true, 2), DEC(true, 2),
	REAL,
	DOUBLE_PRECISION,
	FLOAT(true),
	BOOLEAN,
	DATE,
	TIME,
	TIMESTAMP,
	INTERVAL,

	DATETIME;

	private boolean useParam;
	private int paramCount;

	ColumnType() {}
	ColumnType(boolean useSize) {
		this.useParam = useSize;
		this.paramCount = 1;
	}
	ColumnType(boolean useParam, int paramCount) {
		this.useParam = useParam;
		this.paramCount = paramCount;
	}

}

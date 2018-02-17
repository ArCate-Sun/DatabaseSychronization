package cc.arcate.entity;

/**
 * Created by ACat on 25/01/2018.
 * ACat i lele.
 */
public enum Nullable {
	COLUMN_NO_NULLS, COLUMN_NULLABLE, COLUMN_NULLABLE_UNKNOWN;


	public static Nullable valueOf(int idx) {
		switch (idx) {
			case 0:
				return COLUMN_NO_NULLS;
			case 1:
				return COLUMN_NULLABLE;
			default:
				return COLUMN_NULLABLE_UNKNOWN;
		}
	}
}

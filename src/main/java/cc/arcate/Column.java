package cc.arcate;

/**
 * Created by ACat on 25/01/2018.
 * ACat i lele.
 */
public class Column {
	private String name;
	private Type type;
	private int size;
	private Nullable nullable;
	private boolean primaryKey;
	private boolean autoIncrement;

	public Column() {}

	/**
	 * 拷贝构造函数
	 *
	 * @param column
	 */
	public Column(Column column) {
		this.name = column.name;
		this.type = column.type;
		this.size = column.size;
		this.nullable = column.nullable;
		this.primaryKey = column.primaryKey;
		this.autoIncrement = column.autoIncrement;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Nullable getNullable() {
		return nullable;
	}

	public void setNullable(Nullable nullable) {
		this.nullable = nullable;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
}

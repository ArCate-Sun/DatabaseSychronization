package cc.arcate.config.entity;

import cc.arcate.Column;
import cc.arcate.Nullable;
import cc.arcate.Table;
import cc.arcate.Type;
import cc.arcate.config.impl.XMLConfigLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by ACat on 30/01/2018.
 * ACat i lele.
 */
class LogCfgTest {

	private LogCfg cfg1;
	private LogCfg cfg2;

	LogCfgTest() throws DocumentException {
		String xmlFilePath = Objects.requireNonNull(XMLConfigLoader.class.getClassLoader().getResource("test-log-config.xml")).getPath();
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(xmlFilePath));
		Element root = document.getRootElement();
		this.cfg1 = new LogCfg(root.element("log-1"));
		this.cfg2 = new LogCfg(root.element("log-2"));
	}

	@Test
	void isEnable() {
		assertEquals(true, this.cfg1.isEnable());
		assertEquals(false, this.cfg2.isEnable());
	}

	@Test
	void isEnableLogTable() {
		assertEquals(true, this.cfg1.isEnableLogTable());
		assertEquals(false, this.cfg2.isEnableLogTable());
	}

	@Test
	void getLogTable() {
		Table t1 = this.cfg1.getLogTable();
		Table t2 = this.cfg2.getLogTable();
		assertEquals("log", t1.getName());
		assertEquals("LOG", t2.getName());

		assertEquals(5, t1.getColumns().size());	// 验证日志表中键的个数

		for (Column column : t1.getColumns()) {
			switch (column.getName()) {
				case "id":
					assertEquals(Type.INT, column.getType());
					assertEquals(true, column.isAutoIncrement());
					assertEquals(true, column.isPrimaryKey());
					break;
				case "time":
					assertEquals(Type.DATETIME, column.getType());
					assertEquals(Nullable.COLUMN_NO_NULLS, column.getNullable());
					break;
				case "type":
				case "task":
				case "log":
					assertEquals(Type.CHAR, column.getType());
					assertEquals(255, column.getSize());
					assertEquals(Nullable.COLUMN_NULLABLE, column.getNullable());
					break;
			}
		}

	}
}
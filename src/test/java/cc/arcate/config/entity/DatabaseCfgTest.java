package cc.arcate.config.entity;

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
class DatabaseCfgTest {

	private DatabaseCfg cfg1;
	private DatabaseCfg cfg2;

	DatabaseCfgTest() throws DocumentException {
		String xmlFilePath = Objects.requireNonNull(XMLConfigLoader.class.getClassLoader().getResource("test-database-config.xml")).getPath();
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(xmlFilePath));
		Element root = document.getRootElement();
		this.cfg1 = new DatabaseCfg(root.element("database-1"));
		this.cfg2 = new DatabaseCfg(root.element("database-2"));
	}

	@Test
	void setGlobalMaxConnectionThread() {
		DatabaseCfg.setGlobalMaxConnectionThread(-100);
		assertEquals(10, DatabaseCfg.getGlobalMaxConnectionThread());
		DatabaseCfg.setGlobalMaxConnectionThread(50);
		assertEquals(50, DatabaseCfg.getGlobalMaxConnectionThread());
		DatabaseCfg.setGlobalMaxConnectionThread(null);
		assertEquals(10, DatabaseCfg.getGlobalMaxConnectionThread());
	}

	@Test
	void getGlobalMaxConnectionThread() {
		assertEquals(10, DatabaseCfg.getGlobalMaxConnectionThread());
	}

	@Test
	void getDriver() {
		assertEquals("com.microsoft.sqlserver.jdbc.SQLServerDriver", this.cfg1.getDriver());
		assertEquals("oracle.jdbc.driver.OracleDriver", this.cfg2.getDriver());
	}

	@Test
	void getUrl() {
		assertEquals("jdbc:sqlserver://192.168.0.250:1433;database=RoadCenter", this.cfg1.getUrl());
		assertEquals("jdbc:oracle:thin:@192.168.0.177:1521:orcl", this.cfg2.getUrl());
	}

	@Test
	void getUsername() {
		assertEquals("sa", this.cfg1.getUsername());
		assertEquals("system", this.cfg2.getUsername());
	}

	@Test
	void getPassword() {
		assertEquals("sjzhbkj026", this.cfg1.getPassword());
		assertEquals("root", this.cfg2.getPassword());
	}

	@Test
	void getMaxConnectionThread() {
		assertEquals(50, this.cfg1.getMaxConnectionThread());
		assertEquals(10, this.cfg2.getMaxConnectionThread());
	}
}
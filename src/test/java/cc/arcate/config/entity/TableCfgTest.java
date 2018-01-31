package cc.arcate.config.entity;

import cc.arcate.config.impl.XMLConfigLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by ACat on 30/01/2018.
 * ACat i lele.
 */
class TableCfgTest {

	private TableCfg cfg1;
	private TableCfg cfg2;

	TableCfgTest() throws DocumentException {
		String xmlFilePath = Objects.requireNonNull(XMLConfigLoader.class.getClassLoader().getResource("test-table-config.xml")).getPath();
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(xmlFilePath));
		Element root = document.getRootElement();
		this.cfg1 = new TableCfg(root.element("table-1"));
		this.cfg2 = new TableCfg(root.element("table-2"));
	}

	@Test
	void getGlobalMaxCopyThread() {
		assertEquals(10, TableCfg.getGlobalMaxCopyThread());
	}

	@Test
	void setGlobalMaxCopyThread() {
		TableCfg.setGlobalMaxCopyThread(-100);
		assertEquals(10, TableCfg.getGlobalMaxCopyThread());
		TableCfg.setGlobalMaxCopyThread(50);
		assertEquals(50, TableCfg.getGlobalMaxCopyThread());
		TableCfg.setGlobalMaxCopyThread(null);
		assertEquals(10, TableCfg.getGlobalMaxCopyThread());
	}

	@Test
	void getGlobalMaxCountEachCopy() {
		assertEquals(500, TableCfg.getGlobalMaxCountEachCopy());
	}

	@Test
	void setGlobalMaxCountEachCopy() {
		TableCfg.setGlobalMaxCountEachCopy(-100);
		assertEquals(500, TableCfg.getGlobalMaxCountEachCopy());
		TableCfg.setGlobalMaxCountEachCopy(1000);
		assertEquals(1000, TableCfg.getGlobalMaxCountEachCopy());
		TableCfg.setGlobalMaxCountEachCopy(null);
		assertEquals(500, TableCfg.getGlobalMaxCountEachCopy());
	}

	@Test
	void getGlobalSyncTimeInterval() {
		assertEquals(600000, TableCfg.getGlobalSyncTimeInterval());
	}

	@Test
	void setGlobalSyncTimeInterval() {
		TableCfg.setGlobalSyncTimeInterval(-100);
		assertEquals(600000, TableCfg.getGlobalSyncTimeInterval());
		TableCfg.setGlobalSyncTimeInterval(1000);
		assertEquals(1000, TableCfg.getGlobalSyncTimeInterval());
		TableCfg.setGlobalSyncTimeInterval(null);
		assertEquals(600000, TableCfg.getGlobalSyncTimeInterval());
	}

	@Test
	void isGlobalNeedToCreate() {
		assertEquals(true, TableCfg.isGlobalNeedToCreate());
	}

	@Test
	void setGlobalNeedToCreate() {
		TableCfg.setGlobalNeedToCreate(false);
		assertEquals(false, TableCfg.isGlobalNeedToCreate());
		TableCfg.setGlobalNeedToCreate(null);
		assertEquals(true, TableCfg.isGlobalNeedToCreate());
	}

	@Test
	void getName() {
		assertEquals("TABLE_1", this.cfg1.getName());
		assertEquals("TABLE_2", this.cfg2.getName());
	}

	@Test
	void getSyncType() {
		assertEquals(SyncType.ADDITIONAL_SYNC, this.cfg1.getSyncType());
		assertEquals(SyncType.FORCE_SYNC, this.cfg2.getSyncType());
	}

	@Test
	void getKey() {
		assertEquals("DHM", this.cfg1.getKey());
		assertEquals("id", this.cfg2.getKey());
	}

	@Test
	void getIndex() {
		assertEquals("index_dhm", this.cfg1.getIndex());
		assertEquals("index_id", this.cfg2.getIndex());
	}

	@Test
	void getMaxCopyThread() {
		assertEquals(100, this.cfg1.getMaxCopyThread());
		assertEquals(TableCfg.getGlobalMaxCopyThread(), this.cfg2.getMaxCopyThread());
	}

	@Test
	void getMaxCountEachCopy() {
		assertEquals(100, this.cfg1.getMaxCountEachCopy());
		assertEquals(TableCfg.getGlobalMaxCountEachCopy(), this.cfg2.getMaxCountEachCopy());
	}

	@Test
	void getSyncTimeInterval() {
		assertEquals(5000, this.cfg1.getSyncTimeInterval());
		assertEquals(TableCfg.getGlobalSyncTimeInterval(), this.cfg2.getSyncTimeInterval());
	}

	@Test
	void isNeedToCreate() {
		assertEquals(false, this.cfg1.isNeedToCreate());
		assertEquals(TableCfg.isGlobalNeedToCreate(), this.cfg2.isNeedToCreate());
	}

}
package cc.arcate.config.impl;

import cc.arcate.config.entity.DatabaseCfg;
import cc.arcate.config.entity.LogCfg;
import cc.arcate.config.entity.TableCfg;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by ACat on 31/01/2018.
 * ACat i lele.
 */
class XMLConfigLoaderTest {

	ConfigLoader loader;

	XMLConfigLoaderTest() {
		this.loader = XMLConfigLoader.loadXMLConfigFromResources("test-config.xml");
	}

	@Test
	void globalVariables() {
		assertEquals(100, DatabaseCfg.getGlobalMaxConnectionThread());
		assertEquals(100, TableCfg.getGlobalMaxCopyThread());
		assertEquals(50, TableCfg.getGlobalMaxCountEachCopy());
		assertEquals(10000, TableCfg.getGlobalSyncTimeInterval());
		assertFalse(TableCfg.isGlobalNeedToCreate());
		assertTrue(TableCfg.isGlobalNeedToCopy());
	}

	@Test
	void getAdditionalSyncTableCfgList() {
		List<TableCfg> list = this.loader.getAdditionalSyncTableCfgList();

		assertNotEquals(null, list);
		assertEquals(2, list.size());

		TableCfg cfg = list.get(0);
		assertEquals("O_TRANSACTION", cfg.getName());
		assertEquals(100, cfg.getMaxCopyThread());
		assertEquals(50, cfg.getMaxCountEachCopy());
		assertEquals(10000, cfg.getSyncTimeInterval());
		assertFalse(cfg.isNeedToCreate());
		assertTrue(cfg.isNeedToCopy());

		cfg = list.get(1);
		assertFalse(cfg.isNeedToCopy());
		assertEquals("W_TRANSACTION", cfg.getName());
		assertEquals(10, cfg.getMaxCopyThread());
		assertEquals(500, cfg.getMaxCountEachCopy());
		assertEquals(20000, cfg.getSyncTimeInterval());
		assertTrue(cfg.isNeedToCreate());
		assertFalse(cfg.isNeedToCopy());
	}

	@Test
	void getForceSyncTableCfgList() {
		List<TableCfg> list = this.loader.getForceSyncTableCfgList();

		assertNotEquals(null, list);
		assertEquals(3, list.size());
	}

	@Test
	void getSourceCfgList() {
		List<DatabaseCfg> list = this.loader.getSourceCfgList();

		assertNotEquals(null, list);
		assertEquals(2, list.size());

		DatabaseCfg cfg = list.get(0);
		assertEquals(20, cfg.getMaxConnectionThread());
		cfg = list.get(1);
		assertEquals(100, cfg.getMaxConnectionThread());

	}

	@Test
	void getTargetCfg() {
		DatabaseCfg cfg = this.loader.getTargetCfg();
		assertNotEquals(null, cfg);
		assertEquals("oracle.jdbc.driver.OracleDriver", cfg.getDriver());
	}

	@Test
	void getLogCfg() {
		LogCfg cfg = this.loader.getLogCfg();
		assertNotEquals(null, cfg);
		assertTrue(cfg.isEnable());
		assertTrue(cfg.isEnableLogTable());
		assertEquals("log", cfg.getLogTable().getName());
	}
}
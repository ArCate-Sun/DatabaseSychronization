package cc.arcate.config.impl;

import cc.arcate.config.entity.DatabaseCfg;
import cc.arcate.config.entity.LogCfg;
import cc.arcate.config.entity.TableCfg;
import cc.arcate.config.interf.ConfigLoaderInterf;
import cc.arcate.util.Elements;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * 配置读取器
 * <p>
 * 给定一个配置文件, 从该文件中获得相关配置信息
 * <p>
 * Created by ACat on 15/01/2018.
 * ACat i lele.
 */
public class XMLConfigLoader extends ConfigLoader {

	private Element root;

	protected XMLConfigLoader() {
		super();
	}

	/**
	 * 加载配置文件,
	 * 初始化配置文件的读取.
	 *
	 * @param xmlFilePath
	 * @throws DocumentException
	 */
	private void initXMLConfigLoader(String xmlFilePath) throws DocumentException {
		// 设置配置文件路径
		this.configPath = xmlFilePath;

		//创建SAXReader对象
		SAXReader reader = new SAXReader();
		//读取文件 获取指定节点
		Document document = reader.read(new File(xmlFilePath));
		this.root = document.getRootElement();
	}

	/**
	 * 加载日志配置
	 */
	private void loadLogConfig() {
		Element log = Elements.getElementFromTagSelector(this.root, "log");
		this.logCfg = new LogCfg(log);
	}

	/**
	 * 加载数据库信息
	 */
	private void loadDatabaseConfig() {

		Element databases = Elements.getElementFromTagSelector(this.root, "databases");

		// 全局设置
		Integer globalMaxConnectionThread = Elements.getIntFromTagSelector(databases, "max-connection-thread", null);
		DatabaseCfg.setGlobalMaxConnectionThread(globalMaxConnectionThread);

		Element sources = Elements.getElementFromTagSelector(databases, "sources");
		// 加载所有源数据库设置
		List<Element> sourceList = sources.elements("source");
		for (Element source : sourceList) {
			this.sourceCfgList.add(new DatabaseCfg(source));
		}

		// 加载同步数据库设置
		Element target = Elements.getElementFromTagSelector(databases, "target");
		this.targetCfg = new DatabaseCfg(target);

	}

	/**
	 * 加载数据表配置
	 */
	private void loadTableConfig() {

		Element tables = Elements.getElementFromTagSelector(this.root, "tables");

		// 全局设置
		Integer globalMaxCopyThread = Elements.getIntFromTagSelector(tables, "max-copy-thread", null);
		Integer globalMaxCountEachCopy = Elements.getIntFromTagSelector(tables, "max-count-each-copy", null);
		Integer globalSyncTimeInterval = Elements.getIntFromTagSelector(tables, "sync-time-interval", null);
		Boolean globalNeedToCreate = Elements.getBooleanFromTagSelector(tables, "need-to-create", null);
		Boolean globalNeedToCopy = Elements.getBooleanFromTagSelector(tables, "need-to-copy", null);
		TableCfg.setGlobalMaxCopyThread(globalMaxCopyThread);
		TableCfg.setGlobalMaxCountEachCopy(globalMaxCountEachCopy);
		TableCfg.setGlobalSyncTimeInterval(globalSyncTimeInterval);
		TableCfg.setGlobalNeedToCreate(globalNeedToCreate);
		TableCfg.setGlobalNeedToCopy(globalNeedToCopy);

		List<Element> tableList = tables.elements("table");
		for (Element table : tableList) {
			this.tableCfgList.add(new TableCfg(table));
		}
	}

	/**
	 * 加载配置文件
	 *
	 * @param xmlFilePath 配置文件绝对路径
	 * @return ConfigLoaderInterf 对象
	 */
	public static ConfigLoader loadXMLConfig(String xmlFilePath) {

		XMLConfigLoader loader = new XMLConfigLoader();

		try {
			loader.initXMLConfigLoader(xmlFilePath);
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}

		loader.loadDatabaseConfig();
		loader.loadLogConfig();
		loader.loadTableConfig();

		return loader;
	}

	/**
	 * 从 resources 中加载配置文件
	 *
	 * @param xmlFilePath 配置文件以 resources 为起始路径的相对路径
	 * @return true    读取成功 false	读取失败
	 */
	public static ConfigLoader loadXMLConfigFromResources(String xmlFilePath) {
		return loadXMLConfig(Objects.requireNonNull(XMLConfigLoader.class.getClassLoader().getResource(xmlFilePath)).getPath());
	}

}

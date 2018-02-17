package cc.arcate.util;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 内含对 DOM 节点的一些简便操作方法
 * 需要依赖 dom4j
 *
 * Created by ACat on 26/01/2018.
 * ACat i lele.
 */
public class Elements {
	/**
	 * 从节点内获取字符文本, 若获取失败, 则返回默认文本
	 *
	 * @param e		节点
	 * @param def	默认文本
	 * @return	返回该节点内的文本, 若节点不存在, 则返回默认文本
	 */
	public static String getStringTextFromElement(Element e, String def) {
		if (e != null) {
			return e.getText();
		}
		return def;
	}

	/**
	 * 从节点内获取字符文本, 转换成整型值后返回,
	 * 若查找节点失败或节点内不是整型值, 则返回默认值
	 *
	 * @param e		节点
	 * @param def	默认值
	 * @return	返回该节点内的整型值, 若查找节点失败或节点内不是整型值, 则返回默认值
	 */
	public static Integer getIntFromElement(Element e, Integer def) {
		if (e != null) {
			try {
				return Integer.parseInt(e.getText());
			} catch (Exception ex) {
				return def;
			}
		}
		return def;
	}

	/**
	 * 从节点内获取字符文本, 转换成布尔值后返回,
	 * 若查找节点失败或节点内不是布尔值, 则返回默认值
	 *
	 * @param e		节点
	 * @param def	默认值
	 * @return	返回该节点内的布尔值, 若查找节点失败或节点内不是布尔值, 则返回默认值
	 */
	public static Boolean getBooleanFromElement(Element e, Boolean def) {
		if (e != null) {
			try {
				return Boolean.parseBoolean(e.getText());
			} catch (Exception ex) {
				return def;
			}
		}
		return def;
	}

	/**
	 * 返回根据节点名选择器找到的指定节点
	 *
	 * @param root 			根节点
	 * @param tagSelector	节点名选择器
	 * @return	返回根据节点名选择器找到的指定节点,
	 * 			若未找到, 则返回 null
	 */
	public static Element getElementFromTagSelector(Element root, String tagSelector) {
		String[] tagNameList = tagSelector.split(" ");

		Element element = root;
		for (String tagName : tagNameList) {
			if (element == null) break;
			element = element.element(tagName);
		}

		return element;
	}

	/**
	 * 根据节点名选择器找到指定的节点, 并返回该节点内的文本
	 *
	 * @param root 			根节点
	 * @param tagSelector	节点名选择器
	 * @param def			默认文本
	 * @return	返回该节点内的文本, 若查找节点失败, 则返回默认文本
	 */
	public static String getStringTextFromTagSelector(Element root, String tagSelector, String def) {
		return getStringTextFromElement(getElementFromTagSelector(root, tagSelector), def);
	}

	/**
	 * 根据节点名选择器找到指定的节点, 将该节点内的文本转换为整型值并返回,
	 * 若查找节点失败或节点内不是整型值, 则返回默认值
	 *
	 * @param root 			根节点
	 * @param tagSelector	节点名选择器
	 * @param def			默认值
	 * @return	返回该节点内的整型值, 若查找节点失败或节点内不是整型值, 则返回默认值
	 */
	public static Integer getIntFromTagSelector(Element root, String tagSelector, Integer def) {
		return getIntFromElement(getElementFromTagSelector(root, tagSelector), def);
	}

	/**
	 * 根据节点名选择器找到指定的节点, 将该节点内的文本转换为布尔值并返回,
	 * 若查找节点失败或节点内不是布尔值, 则返回默认值
	 *
	 * @param root 			根节点
	 * @param tagSelector	节点名选择器
	 * @param def			默认值
	 * @return	返回该节点内的布尔值, 若查找节点失败或节点内不是布尔值, 则返回默认值
	 */
	public static Boolean getBooleanFromTagSelector(Element root, String tagSelector, Boolean def) {
		return getBooleanFromElement(getElementFromTagSelector(root, tagSelector), def);
	}

	public static List<Element> getElementsFromTagName(Element root, String tagSelector) {

		String[] tagNameList = tagSelector.split(" ");

		Element element = root;
		int i;
		for (i = 0; i < tagNameList.length - 2; ++i) {
			if (element == null) break;
			element = element.element(tagNameList[i]);
		}
		if (element != null) {
			return element.elements(tagNameList[i]);
		}

		return new ArrayList<>();
	}
}

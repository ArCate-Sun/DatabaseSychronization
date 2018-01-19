package cc.arcate.util;

import java.io.PrintStream;

/**
 * Created by ACat on 18/01/2018.
 * ACat i lele.
 */
public class Log {
	private boolean enable = true;

	public static final String ERR = "[错误] ";
	public static final String EXEC_SQL = "[执行SQL] ";

	private PrintStream out = System.out;


	public void log(String log, String type) {
		if (log.contains("insert")) return;
		if (enable) {
			this.out.println(type + "记录: " + log);
		}
	}

	public void log(String log, String type, String task) {
		if (enable) {
			this.out.println(type + "任务: " + task + " | 记录: " + log);
		}
	}

}

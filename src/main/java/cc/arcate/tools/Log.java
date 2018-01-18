package cc.arcate.tools;

import java.io.PrintStream;

/**
 * Created by ACat on 18/01/2018.
 * ACat i lele.
 */
public class Log {
	private boolean enable = true;

	public static final String EXEC_SQL = "[执行SQL] ";

	private PrintStream out = System.out;


	public void log(String log, String type) {
		if (enable) {
			this.out.println(type + log);
		}
	}
}

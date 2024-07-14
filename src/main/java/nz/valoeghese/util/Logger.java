package nz.valoeghese.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class Logger {
	public Logger(String name) {
		this.name = name;
	}

	private final String name;

	public void info(String message, Object... format) {
		logMessage(0, message, format);
	}

	public void warn(String message, Object... format) {
		logMessage(1, message, format);
	}

	public void error(String message, Throwable t, Object... format) {
		logMessage(2, message, format);
		t.printStackTrace();
	}

	public void error(String message, Object... format) {
		logMessage(2, message, format);
	}

	private void logMessage(int colour, String message, Object[] format) {
		String time = TIME_FORMAT.format(Calendar.getInstance().getTime());

		StringBuilder builder = new StringBuilder();
		builder.append(ANSI_CYAN).append('[').append(time).append(']').append(ANSI_RESET).append(" ");
		builder.append('(').append(this.name).append(')').append(" ");

		switch (colour) {
		case 0:
		default:
			break;
		case 1:
			builder.append(ANSI_YELLOW);
			break;
		case 2:
			builder.append(ANSI_RED);
			break;
		}

		message = message.replaceAll("\\{}", "%s");
		message = String.format(message, format);

		builder.append(message);
		System.out.println(builder);
	}

	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm:ss");
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_RESET = "\u001B[0m";
}

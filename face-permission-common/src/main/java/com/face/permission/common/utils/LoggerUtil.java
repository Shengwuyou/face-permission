package com.face.permission.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LoggerUtil
 *
 * @Author plw
 * @CreateDate 2018年1月5日
 */
public class LoggerUtil {
	/**
	 * 普通日志，请求记录
	 */
	public static final Logger COMMON_DEFAULT = LogManager.getLogger("common-default");

	public static final Logger INTERCEPTOR = LogManager.getLogger("interceptor");

	public static final Logger COMMON_ERROR = LogManager.getLogger("common-error");

	public static final Logger SCHEDULE = LogManager.getLogger("schedule");

	public static final Logger HTTP_REMOTE = LogManager.getLogger("http-remote");

	/**
	 * 以逗号分隔对象
	 *
	 * @param args
	 * @return
	 */
	private static String buildString(Object... args) {
		StringBuilder sb = new StringBuilder();
		// 对象批量输出
		for (int i = 0; i < args.length; i++) {
			// 基本变量与null,String类型
			if (null == args[i] || isPrimitive(args[i])) {
				sb.append(args[i]);
			} else {
				sb.append(JSON.toJSONString(args[i]));
			}
			sb.append(",");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static void info(Logger logger, Object... args) {
		logger.info(buildString(args));
	}

	public static void warn(Logger logger, Object... args) {
		logger.warn(buildString(args));
	}

	public static void warn(Logger logger, Throwable e, Object... args) {
		logger.warn(buildString(args), e);
	}

	public static void error(Logger logger, Throwable e, Object... args) {
		logger.error(buildString(args), e);
	}

	public static void error(Logger logger, Object... args) {
		logger.error(buildString(args));
	}

	/**
	 * 是否为基本数据类型
	 *
	 * @param target
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static boolean isPrimitive(Object target) {
		Class clzz = target.getClass();
		if (Boolean.class.equals(clzz) || Character.class.equals(clzz) || Short.class.equals(clzz) || Integer.class.equals(clzz) || Long.class.equals(clzz)
				|| Float.class.equals(clzz) || Double.class.equals(clzz) || Byte.class.equals(clzz) || String.class.equals(clzz) || clzz.isPrimitive()) {
			return true;
		}
		return false;
	}
}
/**
 * Copyright (c) 2014 ZePan.Co.Ltd. All rights reserved. 
 *
 */
package com.zepan.android.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zhanglei <br>
 *         日期处理工具类
 * @version 1.0 2015/1/15
 */
public class DateUtil {

	/**
	 * 取系统当前时间，并根据指定格式输出
	 * 
	 * @param format
	 *            格式
	 * @return 格式化的日期
	 * */
	public static String getFormatDate(String format) {
		Calendar caderdar = Calendar.getInstance();
		// 将Calendar类型转换成Date类型
		Date date = caderdar.getTime();
		// 设置日期输出的格式
		SimpleDateFormat df = new SimpleDateFormat(format, Locale.CHINA);
		return df.format(date);
	}

	/**
	 * 取当前时段，如0-12，早安；12-18，午安；18-24，晚安
	 * 
	 * @return 1:早安，2：午安，3：晚安
	 * */
	public static int getTimePeriod() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour >= 0 && hour < 12) {
			return 1;
		} else if (hour >= 12 && hour <= 18) {
			return 2;
		} else if (hour > 18 && hour < 24) {
			return 3;
		}
		return 1;
	}

	/**
	 * 返回本月的第一天是星期几
	 * 
	 * @return 0：周日 ，1：周一……
	 * */
	public static int getDayOfWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 返回本月的天数
	 * 
	 * @return 月份天数
	 * */
	public static int getDayCountInMonth() {
		Calendar alendar = Calendar.getInstance(Locale.CHINA);
		return alendar.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 返回本月的所有天数
	 * 
	 * @return 本月天数列表
	 * */
	public static List<Integer> getDaysListInMonth() {
		List<Integer> days = new ArrayList<Integer>();
		for (int i = 1, count = getDayCountInMonth(); i <= count; i++) {
			days.add(i);
		}
		return days;
	}

	/**
	 * {"$date":1083928298294349}->"2015-01-31 10:39"
	 * 
	 * @param dateJson
	 * @param format
	 * @return formatted time
	 * */
	public static String getFormatDate(JSONObject dateJson, String format) {
		if (dateJson != null && dateJson.has("$date")) {
			try {
				String timeLongStr = dateJson.getString("$date");
				SimpleDateFormat sdf = new SimpleDateFormat(format,
						Locale.CHINA);
				return sdf.format(new Date(Long.parseLong(timeLongStr)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}

	public static long getTimeFromJson(JSONObject dateJson) {
		if (dateJson != null && dateJson.has("$date")) {
			try {
				return dateJson.getLong("$date");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0l;
	}

	public static String transfromTimeToPeriod(long time) {
		if (time > 0) {
			long timeDiff = (System.currentTimeMillis() - time) / 1000;
			if (timeDiff >= 24 * 60 * 60) {
				return timeDiff / (24 * 60 * 60) + "天前";
			} else if (timeDiff >= 60 * 60) {
				return timeDiff / (60 * 60) + "小时前";
			} else if (timeDiff >= 60) {
				return timeDiff / 60 + "分钟前";
			} else {
				return "刚刚";
			}
		}
		return null;
	}

	public static String transfromTimeToPeriod(int minute) {
		if (minute > 0) {
			if (minute >= 24 * 60) {
				return minute / (24 * 60) + "天前";
			} else if (minute >= 60) {
				return minute / 60 + "小时前";
			} else if (minute >= 1) {
				return minute + "分钟前";
			} else {
				return "刚刚";
			}
		}
		return null;
	}

	public static String differDate(int diffday) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_WEEK, diffday);
		Date temp_date = c.getTime();
		return format.format(temp_date);
	}
}

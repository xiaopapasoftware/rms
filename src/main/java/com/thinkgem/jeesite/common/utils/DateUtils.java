/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
			"yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
	 * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24) + 1;
	}

	/**
	 * 获取n个月之后的日期
	 * 
	 * @param date
	 * @param addMonth
	 * @return
	 */
	public static Date dateAddMonth(Date date, int addMonth) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(2, addMonth);
		return gc.getTime();
	}
	
	/**
	 * 获取n天之后的日期
	 * 
	 * @param date
	 * @param addMonth
	 * @return
	 */
	public static Date dateAddDay(Date date, int day) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DAY_OF_MONTH, day);
		return gc.getTime();
	}

	/**
	 * 获取n个月之后的日期，要扣除1天。
	 * 
	 * @param date
	 * @param addMonth
	 * @return
	 */
	public static Date dateAddMonth2(Date date, int addMonth) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(2, addMonth);
		Calendar c = Calendar.getInstance();
		c.setTime(gc.getTime());
		c.add(Calendar.DAY_OF_MONTH, -1);
		return c.getTime();
	}

	/**
	 * 计算两个日期之间相隔的月数
	 */
	public static double getMonthSpace(Date startDate, Date endDate) {
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		startCalendar.clear(Calendar.HOUR);
		startCalendar.clear(Calendar.MINUTE);
		startCalendar.clear(Calendar.SECOND);
		startCalendar.clear(Calendar.MILLISECOND);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		endCalendar.clear(Calendar.HOUR);
		endCalendar.clear(Calendar.MINUTE);
		endCalendar.clear(Calendar.SECOND);
		endCalendar.clear(Calendar.MILLISECOND);

		if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)) {
			return calculateSameYearMonthDiff(startCalendar, endCalendar);
		} else {
			// 开始日期所在年份的最后一天日期
			Calendar lastDayOfStartYearCalendar = Calendar.getInstance();
			lastDayOfStartYearCalendar.clear();
			lastDayOfStartYearCalendar.set(Calendar.YEAR, startCalendar.get(Calendar.YEAR));
			lastDayOfStartYearCalendar.roll(Calendar.DAY_OF_YEAR, -1);

			// 结束日期所在年份的第一天的日期
			Calendar firstDayOfEndYearCalendar = Calendar.getInstance();
			firstDayOfEndYearCalendar.clear();
			firstDayOfEndYearCalendar.set(Calendar.YEAR, endCalendar.get(Calendar.YEAR));
			return new BigDecimal(calculateSameYearMonthDiff(startCalendar, lastDayOfStartYearCalendar)
					+ calculateSameYearMonthDiff(firstDayOfEndYearCalendar, endCalendar)).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue()
					+ (endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR) - 1) * 12;
		}
	}

	// 计算同年的任意日期月份间隔数
	private static double calculateSameYearMonthDiff(Calendar startCalendar, Calendar endCalendar) {
		if (startCalendar.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)) {// 同月
			Integer days = endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar.get(Calendar.DAY_OF_MONTH) + 1;// 天数
			double monthdiff = days.doubleValue()
					/ new Integer(endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)).doubleValue();// 比例
			return new BigDecimal(monthdiff).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); // 保留两位小数
		}
		if (startCalendar.get(Calendar.MONTH) < endCalendar.get(Calendar.MONTH)) {// 不同月
			Calendar tempC = Calendar.getInstance();
			tempC.setTime(dateAddMonth(startCalendar.getTime(), 1));// 先加一个月
			tempC.add(Calendar.DAY_OF_MONTH, -1);// 按照房屋租赁习惯，到日期应为系统计算的前一天
			if (tempC.after(endCalendar)) {// 间隔不足一个月，返回月份比例
				Double diffDays = getDistanceOfTwoDate(startCalendar.getTime(), endCalendar.getTime());// 两日期间隔天数
				double towMonthAvgDays = (((Integer) (startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) + endCalendar
						.getActualMaximum(Calendar.DAY_OF_MONTH))).doubleValue()) / 2d; // 前后2个月的平均天数
				return new BigDecimal(diffDays / towMonthAvgDays).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); // 保留两位小数
			}
			if (tempC.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)
					&& tempC.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)
					&& tempC.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH)) {// 开始日期一个月后正好与结束日期相等
				return 1.0f;
			}
			if (tempC.before(endCalendar)) {// 开始日期和结束日期时间间隔超过一个月
				double monthCount = 0d;// 相差月份的整数间隔
				int dayOfMonth = startCalendar.get(Calendar.DAY_OF_MONTH);// 循环外保存日期，防止变化
				if (dayOfMonth == 1) {// 如果恰好是月初第一天，做特殊处理
					while (tempC.before(endCalendar)) {
						monthCount++;
						tempC.add(Calendar.DAY_OF_MONTH, 1);
						tempC.add(Calendar.MONTH, 1);
						tempC.add(Calendar.DAY_OF_MONTH, -1);
					}
					if (tempC.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)
							&& tempC.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)
							&& tempC.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH)) {// 开始日期一个月后正好与结束日期相等
						return monthCount + 1;
					} else {// 计算该日期开始到截止日期的天数，是零头
						tempC.set(Calendar.DAY_OF_MONTH, 1);
						Double diffDays = getDistanceOfTwoDate(tempC.getTime(), endCalendar.getTime());// 两日期间隔天数
						Double towMonthAvgDays = (((Integer) (tempC.getActualMaximum(Calendar.DAY_OF_MONTH) + endCalendar
								.getActualMaximum(Calendar.DAY_OF_MONTH))).doubleValue()) / 2d; // 前后2个月的平均天数
						double remainNum = new BigDecimal(diffDays / towMonthAvgDays).setScale(2,
								BigDecimal.ROUND_HALF_UP).doubleValue(); // 保留两位小数,整月除外的零头的天数
						return monthCount + remainNum;
					}
				} else {
					while (tempC.before(endCalendar)) {
						monthCount++;
						tempC.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						tempC.add(Calendar.MONTH, 1);
						tempC.add(Calendar.DAY_OF_MONTH, -1);
					}
					if (tempC.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)
							&& tempC.get(Calendar.MONTH) == endCalendar.get(Calendar.MONTH)
							&& tempC.get(Calendar.DAY_OF_MONTH) == endCalendar.get(Calendar.DAY_OF_MONTH)) {// 开始日期一个月后正好与结束日期相等
						return monthCount + 1;
					} else {// 计算该日期开始到截止日期的天数，是零头
						tempC.add(Calendar.MONTH, -1);
						tempC.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						Double diffDays = getDistanceOfTwoDate(tempC.getTime(), endCalendar.getTime());// 两日期间隔天数
						Double towMonthAvgDays = (((Integer) (tempC.getActualMaximum(Calendar.DAY_OF_MONTH) + endCalendar
								.getActualMaximum(Calendar.DAY_OF_MONTH))).doubleValue()) / 2d; // 前后2个月的平均天数
						double remainNum = new BigDecimal(diffDays / towMonthAvgDays).setScale(2,
								BigDecimal.ROUND_HALF_UP).doubleValue(); // 保留两位小数,整月除外的零头的天数
						return monthCount + remainNum;
					}
				}
			}
		}
		return 0f;
	}
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(getMonthSpace(sdf.parse("2015-9-30"), sdf.parse("2015-10-29")));
		// System.out.println(sdf.format(dateAddMonth2(sdf.parse("2015-9-27"),
		// 1)));

	}
}

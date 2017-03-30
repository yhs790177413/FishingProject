package com.goby.fishing.common.utils.helper.android.util;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.Log;

public class TimeUtil {

	public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static SimpleDateFormat yearFormat = null;
	private static SimpleDateFormat monthFormat = null;
	private static SimpleDateFormat dayFormat = null;

	public static String formatTime(String time) {

		if (null == time)
			return null;

		String strTime = null;
		try {
			Date date = new Date(time);
			strTime = getFormatTime(date, FORMAT);
		} catch (Exception e) {
			Log.e("tgnet.com", e.getMessage() + "" + " >>>>> " + time);
			return time;
		}

		return strTime;
	}

	/**
	 * 格式化时间
	 * 
	 * @param date
	 *            需要格式的时间
	 * @param strFormat
	 *            转换的格式
	 * @return
	 */
	public static String getFormatTime(Date date, String strFormat) {

		String time;
		SimpleDateFormat format = new SimpleDateFormat(strFormat);
		time = format.format(date);

		return time;
	}

	public static String formatTime(long time, String format) {
		Date date = new Date(time);
		return getFormatTime(date, format);
	}

	public static String getStandardTime(String time) {
		long lTime = 0;
		try {
			lTime = Long.parseLong(time) - 28800000;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(lTime);
		return formatter.format(calendar.getTime());
	}

	/**
	 * 获取处转换过后的当前时间
	 * 
	 * @return
	 */
	public static String getNowTimeStr() {

		Calendar calendar = Calendar.getInstance();
		return getInterval(calendar.getTime());
	}

	/**
	 * 获取yyyy-MM-dd HH:mm:ss的日期部分
	 * 
	 * @return
	 */
	public static String getDateFromStandard(String standard) {
		try {
			String[] date = standard.split(" ");
			return date[0];
		} catch (Exception e) {
			e.printStackTrace();
			return standard;
		}
	}

	/**
	 * 获取格式化后的当前时间 格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getNowTimeFormat() {

		Calendar calendar = Calendar.getInstance();
		return getFormatTime(calendar.getTime(), FORMAT);
	}

	public static String getInterval(String date) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
			Date d = sdf.parse(date);

			return getInterval(d);
		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

	public static String getFindProjectInterval(String date) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
			Date d = sdf.parse(date);

			return getInterval(d);
		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

	public static String getInterval(long time) {

		try {
			Date d = new Date(time - 28800000);
			return getInterval(d);
		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}

	private static SimpleDateFormat smpDFmtY = null;
	private static SimpleDateFormat smpDFmtT = null;
	private static SimpleDateFormat smpDFmtDBTY = null;
	private static SimpleDateFormat smpDFmtDBY = null;

	private static String outputyyyyMMddHHmmFormatTime(Context context,
			long timeInMillis) {
		if (smpDFmtDBTY == null) {
			// smpDFmtDBTY = new SimpleDateFormat(context.getResources()
			// .getString(R.string.chatting_pattern_day_before_this_year));
		}
		return smpDFmtDBTY.format(new Date(timeInMillis));
	}

	private static String outputHHmmYesterday(Context context, long timeInMillis) {
		if (smpDFmtY == null) {
			// smpDFmtY = new SimpleDateFormat(context.getResources().getString(
			// R.string.chatting_pattern_yesterday));
		}
		return smpDFmtY.format(new Date(timeInMillis));
	}

	private static String outputHHMM(Context context, long timeInMillis) {
		if (smpDFmtT == null) {
			// smpDFmtT = new SimpleDateFormat(context.getResources().getString(
			// R.string.chatting_pattern_today));
		}
		return smpDFmtT.format(new Date(timeInMillis));
	}

	private static String outputMMddHHmmFormatTime(Context context,
			long timeInMillis) {
		if (smpDFmtDBY == null) {
			// smpDFmtDBY = new
			// SimpleDateFormat(context.getResources().getString(
			// R.string.chatting_pattern_day_before_yesterday));
		}
		String result = smpDFmtDBY.format(new Date(timeInMillis));
		return result;
	}

	public static String getTimeDetailFromNowInRuleFormat(Context context,
			long timeInMillis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeInMillis);
		int tYear = calendar.get(Calendar.YEAR);
		int tDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.setTimeInMillis(System.currentTimeMillis());
		int curYear = calendar.get(Calendar.YEAR);
		int curDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

		if (curYear - tYear == 0) {
			int daysDistance = curDayOfYear - tDayOfYear;
			if (daysDistance > 1) {
				return outputMMddHHmmFormatTime(context, timeInMillis);
			} else if (daysDistance == 1) {
				return outputHHmmYesterday(context, timeInMillis);
			} else if (daysDistance == 0) {
				return outputHHMM(context, timeInMillis);
			} else {
				return outputyyyyMMddHHmmFormatTime(context, timeInMillis);
			}
		} else {
			return outputyyyyMMddHHmmFormatTime(context, timeInMillis);
		}

	}

	public static String getTimeDetailFromNowInRuleFormat(Context context,
			String timeInString) {
		try {
			return getTimeDetailFromNowInRuleFormat(context,
					new SimpleDateFormat(FORMAT).parse(timeInString).getTime());
		} catch (ParseException e) {
			return null;
		}
	}

	private static SimpleDateFormat smpDFYMD = null;
	private static SimpleDateFormat smpDFMD = null;

	public static String outputMMdd(Context context, long timeInMillis) {
		if (smpDFMD == null) {
			// smpDFMD = new SimpleDateFormat(context.getResources().getString(
			// R.string.chatting_pattern_day_month_day));
		}
		return smpDFMD.format(new Date(timeInMillis));
	}

	public static String outputyyyyMMdd(Context context, long timeInMillis) {
		if (smpDFYMD == null) {
			// smpDFYMD = new SimpleDateFormat(context.getResources().getString(
			// R.string.chatting_pattern_day_year_month_day));
		}
		return smpDFYMD.format(new Date(timeInMillis));
	}

	public static String getDateDetailFromNowInRuleFormat(Context context,
			String timeInString) {
		try {
			long time = new SimpleDateFormat(FORMAT).parse(timeInString)
					.getTime();
			return getDateDetailFromNowInRuleFormat(context, time);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String getDateDetailFromNowInRuleFormat(Context context,
			long timeInMillis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeInMillis);
		int tYear = calendar.get(Calendar.YEAR);
		int tDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.setTimeInMillis(System.currentTimeMillis());
		int curYear = calendar.get(Calendar.YEAR);
		int curDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

		if (curYear - tYear == 0) {
			int daysDistance = curDayOfYear - tDayOfYear;
			if (daysDistance == 0) {
				return outputHHMM(context, timeInMillis);
			} else if (daysDistance > 0) {
				return outputMMdd(context, timeInMillis);
			} else {
				return outputMMdd(context, timeInMillis);
			}
		} else {
			return outputyyyyMMdd(context, timeInMillis);
		}
	}

	public static long transform(String timeInString) {
		try {
			return new SimpleDateFormat(FORMAT).parse(timeInString).getTime();
		} catch (ParseException e) {
			return 0;
		}
	}

	public static String getDateFromNowInRuleFormat(Context context,
			String timeInString) {
		try {
			return getDateFromNowInRuleFormat(context, new SimpleDateFormat(
					FORMAT).parse(timeInString).getTime());
		} catch (ParseException e) {
			return null;
		}
	}

	public static String getDateFromNowInRuleFormat(Context context,
			long timeInMillis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeInMillis);
		int tYear = calendar.get(Calendar.YEAR);
		calendar.setTimeInMillis(System.currentTimeMillis());
		int curYear = calendar.get(Calendar.YEAR);

		if (curYear - tYear == 0) {
			return outputMMdd(context, timeInMillis);
		} else {
			return outputyyyyMMdd(context, timeInMillis);
		}
	}

	/**
	 * 获取处理后的时间
	 * 
	 * @return
	 */
	public static String getInterval(Date date) {
		// 定义最终返回的结果字符串。
		String interval = "";
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		long millisecond = Calendar.getInstance().getTimeInMillis()
				- date.getTime();

		long second = millisecond / 1000;

		if (second <= 0) {
			second = 0;
		}

		// if (second < 60 * 5) {
		// interval = "刚刚";
		// } else
		if (second < 60 * 10) {
			interval = "5分钟前";
		} else if (second < 60 * 30) {
			interval = "10分钟前";
		} else if (second < 60 * 60) {
			interval = "30分钟前";
		} else {

			int hour = c.get(Calendar.HOUR_OF_DAY);
			int day = c.get(Calendar.DAY_OF_MONTH);
			int month = c.get(Calendar.MONTH);
			int year = c.get(Calendar.YEAR);

			String AP = "";

			if (hour <= 12) {
				AP = "上午 ";
			} else {
				AP = "下午";
			}

			if (Calendar.getInstance().get(Calendar.YEAR) == year) {// 同年
				if (Calendar.getInstance().get(Calendar.MONTH) == month) {// 同月
					switch (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
							- day) {
					case 0:// 当天

						interval += AP;
						interval += getFormatTime(date, " HH:mm");

						break;

					case 1:// 昨天

						interval += "昨天 ";
						interval += AP;

						break;
					case 2:// 前天

						interval += "前天 ";
						interval += AP;

						break;

					default:

						interval = getFormatTime(date, "M月dd日");

						break;
					}
				} else {
					interval = getFormatTime(date, "M月dd日");
				}

			} else {
				interval = getFormatTime(date, "yyyy年M月dd日");
			}

		}
		return interval;
	}

	/**
	 * 将时间戳转换成自定义时间格式
	 * 
	 * @param time
	 * @return
	 */
	public static String dateString(String time) {

		Long timestamp = Long.parseLong(time);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(
				timestamp));

	}

	public static String getYear(String time) {
		Long timestamp = Long.parseLong(time);
		return new SimpleDateFormat("yyyy").format(new Date(timestamp));
	}

	/**
	 * 获取年份
	 * 
	 * @param context
	 * @param timeInMillis
	 * @return
	 */
	public static String getYear(long timeInMillis) {
		if (yearFormat == null) {
			yearFormat = new SimpleDateFormat("yyyy");
		}
		return yearFormat.format(new Date(timeInMillis));
	}

	/**
	 * 获取月份
	 * 
	 * @param context
	 * @param timeInMillis
	 * @return
	 */
	public static String getMonth(long timeInMillis) {
		if (monthFormat == null) {
			monthFormat = new SimpleDateFormat("MM");
		}
		return monthFormat.format(new Date(timeInMillis));
	}

	/**
	 * 获取日
	 * 
	 * @param context
	 * @param timeInMillis
	 * @return
	 */
	public static String getDay(long timeInMillis) {
		if (dayFormat == null) {
			dayFormat = new SimpleDateFormat("dd");
		}
		return dayFormat.format(new Date(timeInMillis));
	}

	/**
	 * 通过时间转换返回今天，昨天，前天，其他时间值
	 * 
	 * @param dataString
	 * @return
	 */
	public static String getDayString(String dataString) {
		String dayString = "";
		long longString = stringToLong(dataString, "yyyy-MM-dd");
		if (Integer.parseInt(getYear(getCurrentTime()))
				- Integer.parseInt(getYear(longString)) == 0) {
			if (Integer.parseInt(getMonth(getCurrentTime()))
					- Integer.parseInt(getMonth(longString)) == 0) {
				if (Integer.parseInt(getDay(getCurrentTime()))
						- Integer.parseInt(getDay(longString)) == 0) {
					dayString = "今天";
				} else if (Integer.parseInt(getDay(getCurrentTime()))
						- Integer.parseInt(getDay(longString)) == 1) {
					dayString = "昨天";
				} else if (Integer.parseInt(getDay(getCurrentTime()))
						- Integer.parseInt(getDay(longString)) == 2) {
					dayString = "前天";
				} else {
					dayString = getTimeOne(dataString, "yyyy-MM-dd HH:mm");
				}
			} else {
				dayString = getTimeOne(dataString, "yyyy-MM-dd HH:mm");
			}
		} else {
			dayString = getTimeOne(dataString, "yyyy-MM-dd HH:mm");
		}
		return dayString;
	}

	/**
	 * 通过时间转换返回今天，昨天，前天，其他时间值
	 * 
	 * @param dataString
	 * @return
	 */
	public static String getTimeString(long dataLong) {
		String dayString = "";
		String dataString = formatTime(dataLong, FORMAT);
		if (Integer.parseInt(getYear(getCurrentTime()))
				- Integer.parseInt(getYear(dataLong)) == 0) {
			if (Integer.parseInt(getMonth(getCurrentTime()))
					- Integer.parseInt(getMonth(dataLong)) == 0) {
				if (Integer.parseInt(getDay(getCurrentTime()))
						- Integer.parseInt(getDay(dataLong)) == 0) {
					if (getCurrentTime() - dataLong < 1000 * 60 * 10) {
						dayString = "刚刚";
					} else if (getCurrentTime() - dataLong < 1000 * 60 * 60) {
						dayString = (getCurrentTime() - dataLong) / 1000 / 60
								+ "分钟前";
					} else {
						dayString = (getCurrentTime() - dataLong) / 1000 / 60
								/ 60 + "小时前";
					}
				} else if (Integer.parseInt(getDay(getCurrentTime()))
						- Integer.parseInt(getDay(dataLong)) == 1) {
					dayString = "昨天 " + getTimeOne(dataString, "HH:mm");
				} else {
					dayString = getTimeOne(dataString, "MM-dd HH:mm");
				}
			} else {
				dayString = getTimeOne(dataString, "MM-dd HH:mm");
			}
		} else {
			dayString = getTimeOne(dataString, "yyyy-MM-dd HH:mm");
		}
		return dayString;
	}

	/**
	 * 将时间字符串截取到年月日时分
	 */
	public static String getTimeOne(String dateString, String defaultTime) {
		String time = "";
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = df.parse(dateString);
			SimpleDateFormat dfs = new SimpleDateFormat(defaultTime);
			time = dfs.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 时间类型的转换
	 * 
	 * @param serverTime
	 * @param format
	 * @return
	 */
	public static long stringToLong(String serverTime, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date d = sdf.parse(serverTime);
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取当前的星期几
	 */
	public static String getWeek(String pTime) {

		String Week = "";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {

			c.setTime(format.parse(pTime));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}

		return Week;
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}
}

package com.utils.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.utils.string.StringUtils;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;

public class TimeUtil {
	public static final String TAG = "TimeUtil";

	/**
	 * 获取毫秒的long类型time
	 * 
	 * @param server_time
	 * @return
	 */
	
	public static String SDF_DATE_CHI = "yyyy年MM月dd日";
	public static String SDF_DATE_ENG = "yyyy-MM-dd";
	public static String SDF_DATE_ENG_WITH_MM = "yyyy-MM-dd HH:mm";
	public static String SDF_HH_MM = "HH:mm";
	public static String SDF_YYYY_MM = "yyyy-MM";
	public static String SDF_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	
	public static long getTimeMillis(String server_time) {
		if (StringUtils.isEmpty(server_time)) {
			return 0;
		}
		long time = 0L;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d;
		try {
			d = sdf.parse(server_time);
			return d.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
	public static long getCommunitiesTimeMillis(String server_time) {
		if (StringUtils.isEmpty(server_time)) {
			return 0;
		}
		long time = 0L;
		SimpleDateFormat sdf = new SimpleDateFormat(SDF_DATE_TIME);
		Date d;
		try {
			d = sdf.parse(server_time);
			return d.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
	public static long getTimeMillisWithProject(String server_time) {
		if (StringUtils.isEmpty(server_time)) {
			return 0;
		}
		long time = 0L;
		SimpleDateFormat sdf = new SimpleDateFormat(SDF_DATE_CHI);
		Date d;
		try {
			d = sdf.parse(server_time);
			return d.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
	public static String TimeMillsToString(long TimeMills){
		if (TimeMills!=0) {
			DateFormat formatter = new SimpleDateFormat(SDF_DATE_CHI);
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(TimeMills);
	        System.out.println(TimeMills + " = " + formatter.format(calendar.getTime()));
			return formatter.format(calendar.getTime());
		}
		return "";
	}
	public static String TimeMillsToStringWithMinute(long TimeMills){
		if (TimeMills!=0) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(TimeMills);
	        System.out.println(TimeMills + " = " + formatter.format(calendar.getTime()));
			return formatter.format(calendar.getTime());
		}
		return "";
	}
	/**
	 * 获取date类型的time
	 * 
	 * @param server_time
	 * @return
	 */
	public static Date getDateTime(String server_time) {
		if (StringUtils.isEmpty(server_time)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d;
		try {
			d = sdf.parse(server_time);
			return d;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取date类型的time(年月日格式)
	 * 
	 * @param server_time
	 * @return
	 */
	public static Date getDateTimeWithSDF_DATE_CHI(String server_time) {
		if (StringUtils.isEmpty(server_time)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(SDF_DATE_CHI);
		Date d;
		try {
			d = sdf.parse(server_time);
			return d;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得当天0点时间
	 * 
	 * @return
	 */
	public static long getTimesmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * 获得昨天0点时间
	 * 
	 * @return
	 */
	public static long getTimesYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.DAY_OF_MONTH - 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * 服务器时间与本地时间对比
	 * 
	 * @param server_time
	 * @return
	 */
	public static String TimeFormat(String server_time) {
		if (StringUtils.isEmpty(server_time)) {
			return "";
		}
		long MINUTE = 60000;
		Calendar currCalendar = Calendar.getInstance();

		long serverTime = getTimeMillis(server_time);

		Calendar serverCalendar = Calendar.getInstance();
		currCalendar.setTimeInMillis(serverTime);

		long currTime = System.currentTimeMillis();
		currCalendar.setTimeInMillis(currTime);

		/**
		 * 加一个10秒钟的容错处理
		 */
		long diffTime = currTime - serverTime + 10000;
		if (diffTime < 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
			String time = sdf.format(new Date(serverTime));
			if (time.contains("年0"))
				time = time.replace("年0", "年");
			if (time.contains("月0"))
				time = time.replace("月0", "月");
			if (time.contains("日 0"))
				time = time.replace("日 0", "日 ");
			return time;
		} else if (diffTime > 0 && diffTime < MINUTE) {
			return "现在";
		} else if (diffTime >= MINUTE && diffTime < MINUTE * 60) {
			SimpleDateFormat sdf = new SimpleDateFormat("mm分钟前");
			String time = sdf.format(new Date(diffTime));
			if (time.startsWith("0"))
				time = time.substring(1);
			return time;
		} else if (diffTime >= MINUTE * 60 && diffTime < MINUTE * 60 * 24
				&& serverTime >= getTimesmorning()) {
			SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
			String time = sdf.format(new Date(serverTime));
			if (time.contains("今天 0"))
				time = time.replace("今天 0", "今天 ");
			return time;
		} else if (diffTime >= MINUTE * 60 && diffTime < MINUTE * 60 * 24 * 2
				&& serverTime < getTimesmorning()
				&& serverTime >= getTimesYesterday()) {
			SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
			String time = sdf.format(new Date(serverTime));
			if (time.contains("昨天 0"))
				time = time.replace("昨天 0", "昨天 ");
			return time;
		} else {
			SimpleDateFormat year = new SimpleDateFormat("yyyy");
			// 如果服务器的年份与本地年份相同 则仅显示月日
			if (year.format(new Date(getTimeMillis(server_time))).equals(
					year.format(new Date(currTime)))) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
				String time = sdf.format(new Date(serverTime));
				/*if (time.startsWith("0"))
					time = time.substring(1);
				if (time.contains("月0"))
					time = time.replace("月0", "月");
				if (time.contains("日 0"))
					time = time.replace("日 0", "日 ");*/
				return time;
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
			String time = sdf.format(new Date(serverTime));
			/*if (time.contains("年0"))
				time = time.replace("年0", "年");
			if (time.contains("月0"))
				time = time.replace("月0", "月");
			if (time.contains("日 0"))
				time = time.replace("日 0", "日 ");*/
			return time;
		}
	}

	/** 上次点击时间 */
	private static long lastClickTime;

	/**
	 * 判断是否快速双击
	 * 
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		String MSG = "isFastDoubleClick()";
		long time = System.currentTimeMillis();

		long timeD = time - lastClickTime;
		Log.i(TAG, MSG + " timeD = " + timeD);

		if (0 < timeD && timeD < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/** 格式化日期 yyyy年MM月dd日 E HH:mm */
	public static String formartTime(String str) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy年MM月dd日 E HH:mm");
		try {
			if (false == str.isEmpty()) {
				Date tmp = (fmt.parse(str));
				return dateFormat.format(tmp);
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return "";
	}
	/** 格式化日期 yyyy年MM月dd日 E HH:mm */
	public static String formartTimeWithOther(String str) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM");
		try {
			if (false == str.isEmpty()) {
				Date tmp = (fmt.parse(str));
				return dateFormat.format(tmp);
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return "";
	}

	/** 格式化日期 yyyy年MM月dd日 HH:mm */
	public static String formartTimeWithOutWeek(String str) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		try {
			if (false == str.isEmpty()) {
				Date tmp = (fmt.parse(str));
				return dateFormat.format(tmp);
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return "";
	}

	/* * 输入某年某月某日，判断这一天是这一年的第几天？ */
	public static int getDateDays(String date1, String date2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd");
		try {
			Date date = sdf.parse(date1);// 通过日期格式的parse()方法将字符串转换成日期
			Date dateBegin = sdf.parse(date2);
			long betweenTime = date.getTime() - dateBegin.getTime();
			betweenTime = betweenTime / 1000 / 60 / 60 / 24;
			return (int) betweenTime;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 10000;
	}
	
	public static String getWeek() {
		String mWay;
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(mWay)) {
			mWay = "日";
		} else if ("2".equals(mWay)) {
			mWay = "一";
		} else if ("3".equals(mWay)) {
			mWay = "二";
		} else if ("4".equals(mWay)) {
			mWay = "三";
		} else if ("5".equals(mWay)) {
			mWay = "四";
		} else if ("6".equals(mWay)) {
			mWay = "五";
		} else if ("7".equals(mWay)) {
			mWay = "六";
		}
		return "星期" + mWay;
	}
	
	/**
	 * 格式：sdf_str
	 * @return
	 */
	public static String getDate(Date date, String sdf_str) {
		SimpleDateFormat sdf = new SimpleDateFormat(sdf_str);
		return sdf.format(date);
	}
	public static String getDate(String date, String sdf_str) {
		SimpleDateFormat sdf = new SimpleDateFormat(sdf_str);
		Date d = new Date(Long.valueOf(date));
		return sdf.format(d);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static boolean isSameDay(Date date, String sameDate) {
		try {
			if (null == date || null == sameDate) {
				return false;
			}
			Calendar nowCalendar = Calendar.getInstance();
			nowCalendar.setTime(getDateTime(sameDate));
			Calendar dateCalendar = Calendar.getInstance();

			dateCalendar.setTime(date);
			if (nowCalendar.get(Calendar.YEAR) == dateCalendar
					.get(Calendar.YEAR)
					&& nowCalendar.get(Calendar.MONTH) == dateCalendar
							.get(Calendar.MONTH)
					&& nowCalendar.get(Calendar.DATE) == dateCalendar
							.get(Calendar.DATE)) {

				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}

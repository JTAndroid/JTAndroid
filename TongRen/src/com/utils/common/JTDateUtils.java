package com.utils.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.tr.R;
import com.utils.log.KeelLog;
import com.utils.string.StringUtils;

/**
 * 对日期操作的工具类
 * @author Tony
 *
 */
public class JTDateUtils
{
    /**
     * 指定日期格式 yyyyMMddHHmmss
     */
    public static final String DATE_FORMAT_1 = "yyyyMMddHHmmss";

    /**
     * 指定日期格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_FORMAT_2 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 指定日期格式 yyyy-MM-dd'T'HH:mm:ssZ
     */
    public static final String DATE_FORMAT_3 = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * 指定日期格式 yyyy-MM-dd
     */
    public static final String DATE_FORMAT_4 = "yyyy-MM-dd";
    
    /**
     * 指定日期格式 yyyy.M.d
     */
    public static final String DATE_FORMAT_5 = "yyyy.M.d";
    
    /**
     * 指定日期格式 yyyy-MM-dd HH:mm
     */
    public static final String DATE_FORMAT_6 = "yyyy年MM月dd日 HH:mm";
    
    /**
     * 日期排序类型-升序
     */
    public final static int DATE_ORDER_ASC = 0;

    /**
     * 日期排序类型-降序
     */
    public final static int DATE_ORDER_DESC = 1;

    /**
     * 根据指定格式，获取现在时间
     */
    public static String getNowDateFormat(String format) {
        final Date currentTime = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(currentTime);
    }
    
    public static String getDateFormat(String getDateString, String format) {
        if (!StringUtils.isEmpty(getDateString)) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

            Date getDate = null;
            try {
                getDate = getFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(getDateString);
            } catch (ParseException e) {
                return getDateString;
            }

            return simpleDateFormat.format(getDate);
        } else {
            return "";
        }
    }

    
    /**
     * 根据时间戳转成指定的format格式
     * @param timeMillis
     * @param format
     * @return
     */
    public static String formatDate(String timeMillis, String format) {
        Date date = null;
        if (!StringUtils.isEmpty(timeMillis)) {
            try
            {
                date = new Date(Long.parseLong(timeMillis));
            }
            catch (NumberFormatException e)
            {
                if (KeelLog.DEBUG) {
                    e.printStackTrace();
                }
                date = new Date();
            }
        } else {
            date = new Date();
        }
        
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
    
 

  

    /**
     * 当前时间计算
     * 
     * @param getDateString
     * @return
     */
    public static String getTimeDisplay(String getDateString, Context context) {
        return getTimeDisplay(getDateString, context, false);
    }
    
    /**
     * 返回两个时间的差距，单位s， begin为前， end为后
     * @param begin
     * @param end
     * @return
     */
    public static long getIntervalMoreTime(String begin, String end){
    	try{
    		Date beginDate  = getFormat("yyyy-MM-dd HH:mm:ss").parse(begin);
    		Date endDate  = getFormat("yyyy-MM-dd HH:mm:ss").parse(end);
    		
    		long seconds = (endDate.getTime() - beginDate.getTime()) / 1000;
    		return seconds;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return 0;
    }
    
    public static String getTimeDisplay(String getDateString, Context context, boolean timeZone) {
    	Date getDate = null;
        try {
            if (timeZone) {
            	getDate = getFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(getDateString);
            } else {
            	//getDate = getFormat("yyyyMMdd'T'HH:mm:ss").parse(getDateString);
            	getDate = getFormat("yyyy-MM-dd HH:mm:ss").parse(getDateString);
            }
        } catch (ParseException e) {
            getDate = new Date();
        }

        final long getTime = getDate.getTime();

        final long currTime = System.currentTimeMillis();
        final Date formatSysDate = new Date(currTime);

        // 判断当前总天数
        final int sysMonth = formatSysDate.getMonth() + 1;
        final int sysYear = formatSysDate.getYear();
        long seconds = 0;
        if(currTime >= (getTime-60*1000)){
        	// 计算服务器返回时间与当前时间差值
        	seconds = (currTime - getTime) / 1000;
        
        final long minute = seconds / 60;
        final long hours = minute / 60;
        final long day = hours / 24;
        final long month = day / calculationDaysOfMonth(sysYear, sysMonth);
        final long year = month / 12;

        if (year > 0 || month > 0 || day > 0) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm");
            return simpleDateFormat.format(getDate);
        } else if (hours > 0) {
            return hours + context.getString(R.string.str_hoursago);
        } else if (minute > 0) {
            return minute + context.getString(R.string.str_minsago);
        } else if (seconds > 0) {
            return "1" + context.getString(R.string.str_minsago);
            // return seconds + context.getString(R.string.str_secondago);
        } else {
//          return "1" + context.getString(R.string.str_secondago);
            return "1" + context.getString(R.string.str_minsago); //都换成分钟前
        }
        }else{
        	//如果开始时间在未来
        	  seconds = (getTime - currTime) / 1000;
        	   long minute = seconds / 60;
               long hours = minute / 60;
               long day = hours / 24;
               long month = day / calculationDaysOfMonth(sysYear, sysMonth);
               long year = month / 12;
              
               hours %= 24;
               minute %= 60;
               seconds %= 60;

               String ret = "将于";
//              if (year > 0 || month > 0) {
//                  final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//                          "yyyy-MM-dd HH:mm");
//                  return simpleDateFormat.format(getDate);
//              } 
              if(day > 0){
            	  ret +=  day + "天";
              }
              if (hours > 0) {
            	  ret +=  hours + "小时";
              } 
              if (minute > 0) {
            	  ret +=   minute + "分钟";
              } 
//              if (seconds > 0) {
//            	  ret +=   seconds + "秒";
//              } 
              ret += "后开始";
              return ret;
        }
    }
    
    //畅聊列表的时间显示方式
    public static String getIMListTimeDisplay(String getDateString, Context context, boolean timeZone) {
    	Date getDate = null;
        try {
            if (timeZone) {
            	getDate = getFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(getDateString);
            } else {
            	//getDate = getFormat("yyyyMMdd'T'HH:mm:ss").parse(getDateString);
            	getDate = getFormat("yyyy-MM-dd HH:mm:ss").parse(getDateString);
            }
        } catch (ParseException e) {
            getDate = new Date();
        }

        final long getTime = getDate.getTime();

        final long currTime = System.currentTimeMillis();
        final Date formatSysDate = new Date(currTime);

        // 判断当前总天数
        final int sysMonth = formatSysDate.getMonth() + 1;
        final int sysYear = formatSysDate.getYear();
        long seconds = 0;
        if(currTime >= (getTime-60*1000)){
        	// 计算服务器返回时间与当前时间差值
        	seconds = (currTime - getTime) / 1000;
        
        final long minute = seconds / 60;
        final long hours = minute / 60;
        final long day = hours / 24;
        final long month = day / calculationDaysOfMonth(sysYear, sysMonth);
        final long year = month / 12;

        if (year > 0 || month > 0 || day > 0) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "M月d日");
            return simpleDateFormat.format(getDate);
        } else if (hours > 0) {
            return hours + context.getString(R.string.str_hoursago);
        } else if (minute > 0) {
            return minute + context.getString(R.string.str_minsago);
        } else if (seconds > 0) {
            return "1" + context.getString(R.string.str_minsago);
            // return seconds + context.getString(R.string.str_secondago);
        } else {
//          return "1" + context.getString(R.string.str_secondago);
            return "1" + context.getString(R.string.str_minsago); //都换成分钟前
        }
        }else{
        	return "";
        }
    }
    
    public static String getIMTimeDisplay(String getDateString, Context context, boolean timeZone) {
    	Date getDate = null;
        try {
            if (timeZone) {
            	getDate = getFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(getDateString);
            } else {
            	//getDate = getFormat("yyyyMMdd'T'HH:mm:ss").parse(getDateString);
            	getDate = getFormat("yyyy-MM-dd HH:mm:ss").parse(getDateString);
            }
        } catch (ParseException e) {
            getDate = new Date();
        }
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "MM月dd日 HH:mm");
        return simpleDateFormat.format(getDate);
//        
//
//        final long getTime = getDate.getTime();
//
//        final long currTime = System.currentTimeMillis();
//        final Date formatSysDate = new Date(currTime);
//
//        // 判断当前总天数
//        final int sysMonth = formatSysDate.getMonth() + 1;
//        final int sysYear = formatSysDate.getYear();
//        long seconds = 0;
//        if(currTime > getTime){
//        	// 计算服务器返回时间与当前时间差值
//        	seconds = (currTime - getTime) / 1000;
//        
//        final long minute = seconds / 60;
//        final long hours = minute / 60;
//        final long day = hours / 24;
//        final long month = day / calculationDaysOfMonth(sysYear, sysMonth);
//        final long year = month / 12;
//
//        if (year > 0 || month > 0 || day > 2) {
//            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//                    "MM月dd日 HH:mm");
//            return simpleDateFormat.format(getDate);
//        }  if (year > 0 || month > 0 || day > 1) {
//            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//                    "前天 HH:mm");
//            return simpleDateFormat.format(getDate);
//        }   if (year > 0 || month > 0 || day > 0) {
//            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//                    "昨天 HH:mm");
//            return simpleDateFormat.format(getDate);
//        }  else if (hours > 0) {
//        	final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//                    "HH:mm");
//            return simpleDateFormat.format(getDate);
//        } else if (minute > 0) {
//            return minute + context.getString(R.string.str_minsago);
//        } else if (seconds > 0) {
//            return "1" + context.getString(R.string.str_minsago);
//            // return seconds + context.getString(R.string.str_secondago);
//        } else {
////          return "1" + context.getString(R.string.str_secondago);
//            return "1" + context.getString(R.string.str_minsago); //都换成分钟前
//        }
//        }else{
//        	//如果开始时间在未来
//             return "";
//        }
    }
    
    
    public static String getDateString(String source, Context context) {
    	
    	
    	String dateString = "";
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
    	simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	
		
		Date sourceDate = null;
		try {
			sourceDate = simpleDateFormat.parse(source);
		} catch (ParseException e) {
			if(KeelLog.DEBUG){
				KeelLog.e("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~getDateString---ParseException = " + e.toString());
			}
			sourceDate = new Date();
		}
		long sourceTimeMillis = sourceDate.getTime();
		long currentTimeMillis = System.currentTimeMillis();
		
		if (currentTimeMillis < sourceTimeMillis) {
			dateString = new SimpleDateFormat("HH:mm").format(sourceDate);
			return dateString;
		}
		
		Date currentDate = new Date();
		Calendar sourceCalendar = Calendar.getInstance();
		sourceCalendar.setTime(sourceDate);
		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTime(currentDate);
		
		int sourceYear = sourceCalendar.get(Calendar.YEAR);
		int currentYear = currentCalendar.get(Calendar.YEAR);
		
		int sourceDay = sourceCalendar.get(Calendar.DAY_OF_YEAR);
		int currentDay = currentCalendar.get(Calendar.DAY_OF_YEAR);
		
		if (sourceYear < currentYear) { //判断是否跨年
			currentDay = currentDay + sourceCalendar.getActualMaximum(Calendar.DAY_OF_YEAR);
		}
		
		int offsetDay = currentDay - sourceDay;
		
		if (offsetDay <= 0) {
			dateString = new SimpleDateFormat("HH:mm").format(sourceDate);
			
		} else if (offsetDay == 1) {
			dateString = context.getString(R.string.str_yesterday) + " " 
			                       + new SimpleDateFormat("HH:mm").format(sourceDate);
			
		} else if (offsetDay > 1 && offsetDay <= 7) {
			String weekArray[] = context.getResources().getStringArray(R.array.week_array);
			dateString = weekArray[sourceCalendar.get(Calendar.DAY_OF_WEEK) - 1] 
			                       + " " + new SimpleDateFormat("HH:mm").format(sourceDate);
			
		} else {
			dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(sourceDate);
		}
		
		return dateString;
	}
    
    

    public static SimpleDateFormat getFormat(String partten) {
        return new SimpleDateFormat(partten);
    }

    public static String getFormatDataString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
    
    public static String parseJsonDate(String date) {
        return date.substring(0, 10);
    }
    
    /**
     * 计算月数
     * 
     * @return
     */
    private static int calculationDaysOfMonth(int year, int month) {
        int day = 0;
        switch (month) {
        // 31天
        case 1:
        case 3:
        case 5:
        case 7:
        case 8:
        case 10:
        case 12:
            day = 31;
            break;
        // 30天
        case 4:
        case 6:
        case 9:
        case 11:
            day = 30;
            break;
        // 计算2月天数
        case 2:
            day = year % 100 == 0 ? year % 400 == 0 ? 29 : 28
                    : year % 4 == 0 ? 29 : 28;
            break;
        }

        return day;
    }
    
    /**
     * 日期排序
     * 
     * @param dates
     *            日期列表
     * @param orderType
     *            排序类型：DATE_ORDER_ASC，DATE_ORDER_DESC
     * @return 排序后的list
     * 
     *         用法 ArrayList<Date> dates = new ArrayList<Date>(); String dateStr
     *         = "2011-10-25T00:00:00+08:00"; Date getDate =
     *         getFormat("yyyy-MM-dd").parse(dateStr); dates.add(getDate);
     * 
     *         orderDate(dates, DATE_ORDER_ASC);
     * 
     */
    public static ArrayList<Date> orderDate(ArrayList<Date> dates, int orderType) {
        final DateComparator comp = new DateComparator(orderType);
        Collections.sort(dates, comp);
        return dates;
    }

    static class DateComparator implements Comparator<Date> {
        int orderType;

        public DateComparator(int orderType) {
            this.orderType = orderType;
        }

        @Override
        public int compare(Date d1, Date d2) {
            if (d1.getTime() > d2.getTime()) {
                if (orderType == DATE_ORDER_ASC) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                if (d1.getTime() == d2.getTime()) {
                    return 0;
                } else {
                    if (orderType == DATE_ORDER_DESC) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        }
    }
    
    /***
     * 返回日期 ，如果参数为0返回今天。
     * */
    public static String getNowDate(int afterDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, afterDay);
        return dateFormat.format(calendar.getTime());
    }

    public static Calendar getNowCalendar(int afterDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, afterDay);
        return calendar;
    }

    /**
     * 计算天数方法
     * 
     * @param start
     * @param end
     * @return
     */
    public static int getDaysBetween(String start, String end) {
        int days = 0;
        
        boolean isNegative=false; 
        if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(end)) {
            try {
                final SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd");
                final Date startDate = format.parse(start);

                Calendar startTime = Calendar.getInstance();
                startTime.clear();
                startTime.setTime(startDate);

                final Date endDate = format.parse(end);
                Calendar endTime = Calendar.getInstance();
                endTime.clear();
                endTime.setTime(endDate);

                if (startTime.after(endTime)) {
                    java.util.Calendar swap = startTime;
                    startTime = endTime;
                    endTime = swap;
                    isNegative=true;
                    
                }
                days = endTime.get(Calendar.DAY_OF_YEAR)
                        - startTime.get(Calendar.DAY_OF_YEAR);
                int y2 = endTime.get(Calendar.YEAR);
                if (startTime.get(Calendar.YEAR) != y2) {
                    startTime = (Calendar) startTime.clone();
                    do {
                        days += startTime
                                .getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天数
                        startTime.add(Calendar.YEAR, 1);
                    } while (startTime.get(Calendar.YEAR) != y2);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
          
        if(isNegative&&days!=0){
            return -days;
        }else{
            return days;
        }
    }
    
    /**
     * 把yyyy-MM-dd'T'HH:mm:ssZ类型日期转成yyyy.MM.dd类型
     * @param str
     * @return
     */
    public static String parseStrToDate(String str) {
        if (str != null && str.length() > 0) {
            Date date = null;
            try {
                date = getFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(str);
            } catch (Exception ex) {
                if (KeelLog.DEBUG) {
                    ex.printStackTrace();
                }
            }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "yyyy.MM.dd");
            return simpleDateFormat.format(date);
        } else {
            return null;
        }
    }

//    /**
//     * 根据日期获取星期
//     * 
//     * @param str
//     * @return
//     */
//    public static String getWeekday(String str, Context context) {
//        try {
//            final Date date = getFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(str);
//            if (date != null) {
//                return weekdayFormat(date.getDay(), context); // 0..6 0:sunday,
//            }
//        } catch (ParseException e) {
//            if (KeelLog.DEBUG) {
//                e.printStackTrace();
//            }
//        }
//        return "";
//    }
    
//    private static String weekdayFormat(int day, Context context) {
//        switch (day) {
//        case 0:
//            return context.getString(R.string.str_sunday);
//        case 1:
//            return context.getString(R.string.str_monday);
//        case 2:
//            return context.getString(R.string.str_tuesday);
//        case 3:
//            return context.getString(R.string.str_wednesday);
//        case 4:
//            return context.getString(R.string.str_thursday);
//        case 5:
//            return context.getString(R.string.str_friday);
//        case 6:
//            return context.getString(R.string.str_saturday);
//        default:
//            return "";
//        }
//    }

    /**
     * 获取日期显示
     * 
     * */

    public static String getDateIncludeWeek(Calendar endTime) {
        final int afterDay = getDaysBetween(endTime);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Calendar calendar = Calendar.getInstance();
        // calendar.add(Calendar.DAY_OF_MONTH, afterDay);
        switch (afterDay) {
        case -1:
            return dateFormat.format(endTime.getTime()) ;
        case 0:
            return dateFormat.format(endTime.getTime()) + " 今天";
        case 1:
            return dateFormat.format(endTime.getTime()) + " 明天";
        case 2:
            return dateFormat.format(endTime.getTime()) + " 后天";
        }
        return dateFormat.format(endTime.getTime()) + " " + getWeekDay(endTime);
    }

    /**
     * 去掉星期只截取日期
     * */

    public static String getDateNoWeek(String temp) {
        if (temp != null && temp.length() > 10) {
            return temp.substring(0, 10);
        }
        return temp;
    }

    public static String getDateNoWeek(TextView tv) {
    	if (tv != null) {
    		final String temp = tv.getText().toString();
    		return getDateNoWeek(temp);
    	}
    	return "";
    }

    /**
     * 把2012-02-20 星期一 转化Date 对象
     * @param temp
     * @return
     */
    public static Date String2Date(String temp) {
        String str = getDateNoWeek(temp);
        if (str != null && str.length() == 10) {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            try {
                return format.parse(str);
            } catch (ParseException e) {
                if (KeelLog.DEBUG) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * 返回指定格式的date对象
     * 
     * @param strDate
     * @param strFormat
     * @return
     */
    public static Date getString2FormatDate(String strDate, String strFormat) {
        if (!TextUtils.isEmpty(strDate)) {
            try {
                final SimpleDateFormat format = new SimpleDateFormat(strFormat);
                return format.parse(strDate);
            } catch (ParseException e) {
                if (KeelLog.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 从当天开始计算天数
     * 
     * @param start
     * @param end
     * @return
     */
    public static int getDaysBetween(Calendar endTime) {
        int days = 0;
        boolean isNegative = false; 
        Calendar startTime = Calendar.getInstance();
        try {
            if (startTime.after(endTime)) {
                java.util.Calendar swap = startTime;
                startTime = endTime;
                endTime = swap;
                isNegative=true;
            }
            days = endTime.get(Calendar.DAY_OF_YEAR) - startTime.get(Calendar.DAY_OF_YEAR);
            int y2 = endTime.get(Calendar.YEAR);
            if (startTime.get(Calendar.YEAR) != y2) {
                startTime = (Calendar) startTime.clone();
                do {
                    days += startTime.getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天数
                    startTime.add(Calendar.YEAR, 1);
                } while (startTime.get(Calendar.YEAR) != y2);
            }
        } catch (Exception e) {
            if (KeelLog.DEBUG) {
                e.printStackTrace();
            }
        }

        if (isNegative && days != 0) {
            return -days;
        } else {
            return days;
        }
    }

    /**
     * 获取日期星期
     * @param c
     * @return
     */
    private static String getWeekDay(Calendar c) {
        if (c == null) {
            return "周一";
        }
        if (Calendar.MONDAY == c.get(Calendar.DAY_OF_WEEK)) {
            return "周一";
        }
        if (Calendar.TUESDAY == c.get(Calendar.DAY_OF_WEEK)) {
            return "周二";
        }
        if (Calendar.WEDNESDAY == c.get(Calendar.DAY_OF_WEEK)) {
            return "周三";
        }
        if (Calendar.THURSDAY == c.get(Calendar.DAY_OF_WEEK)) {
            return "周四";
        }
        if (Calendar.FRIDAY == c.get(Calendar.DAY_OF_WEEK)) {
            return "周五";
        }
        if (Calendar.SATURDAY == c.get(Calendar.DAY_OF_WEEK)) {
            return "周六";
        }
        if (Calendar.SUNDAY == c.get(Calendar.DAY_OF_WEEK)) {
            return "周日";
        }
        return "星期一";
    }

    /**
     * 用于计算两个"2012-02-12  星期一"格式 日期之间间隔天数
     * 
     * @author Administrator
     * @param start
     * @param end
     * @return
     * */
    public static String getDaysBetweenForContainWeek(String start, String end) {
        return getDaysBetween(getDateNoWeek(start), getDateNoWeek(end)) + "天";
    }

    /**
     * 用于计算两个"2012-02-12  星期一"格式 日期之间间隔天数
     * 
     * @author Administrator
     * @param start
     * @param end
     * @return
     * */
    public static String getDaysBetweenForContainWeek(TextView start,
            TextView end) {
        return getDaysBetween(getDateNoWeek((start.getText().toString())),
                getDateNoWeek((end.getText().toString()))) + "天";
    }

    /**
     * 将TextView里字符串转换成Calendar
     * 
     * */
    public static Calendar viewText2Calendar(TextView tv) {
        final String date = tv != null ? tv.getText().toString() : "";
        return string2Calendar(date);
    }

   /**
    * 将字符串转换成Calendar
    * @param date
    * @return
    */
    public static Calendar string2Calendar(String date) {
        date = getDateNoWeek(date);
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            d = new Date();
        }
        final Calendar mCalendar = (Calendar) Calendar.getInstance().clone();
        mCalendar.setTime(d);
        return mCalendar;
    }

    /**
     * 通过2012-03-07 获取 2012-03-07 星期三
     * @param endTime
     * @return
     */
    public static String getDateIncludeWeek(String endTime) {
        final Calendar endTimeCalendar = string2Calendar(endTime);
        return getDateIncludeWeek(endTimeCalendar);
    }

    /**
     * 日期大小比较
     * @param oldDate
     * @param defDate
     * @return oldDate小于defDate
     */
    public static boolean compareToDate(String oldDate, String defDate) {
    	if(KeelLog.DEBUG){
    		KeelLog.e("oldDate = " + oldDate + ", defDate" + defDate);
    	}
        if (TextUtils.isEmpty(oldDate) || TextUtils.isEmpty(defDate)) {
            return true;
        }
        
        final Calendar c1 = Calendar.getInstance();
        final Calendar c2 = Calendar.getInstance();

        try { 
            final DateFormat df = new SimpleDateFormat(DATE_FORMAT_3);
            c1.setTime(df.parse(oldDate));
            c2.setTime(df.parse(defDate));
        } catch(Exception e) {
            if (KeelLog.DEBUG) {
                e.printStackTrace();
            }
        }

        final int result = c1.compareTo(c2);
        if (result < 0) {
            return true;
        }
        return false;
        
//        if (result == 0) {
//            KeelLog.e("c1相等c2");
//        } else if(result<0) {
//            KeelLog.e("c1小于c2");
//        } else {
//            KeelLog.e("c1大于c2");
//        }
    }
    
    /**
     * 获得没有分的时间
     * @param time
     * @return
     */
    public static String getTimewithHHmm(String oldStr){
    	if(TextUtils.isEmpty(oldStr)){
    		return new Date().toLocaleString();
    	}
    	SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String newDate = null;
		try {
			Date oldDate = format.parse(oldStr);
			newDate = format.format(oldDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newDate;
    }
    
}

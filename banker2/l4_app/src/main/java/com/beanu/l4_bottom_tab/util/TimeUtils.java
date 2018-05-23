package com.beanu.l4_bottom_tab.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Beanu on 2017/5/16.
 */

public class TimeUtils {

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    /**
     * 字符串转Date
     *
     * @param user_time yyyy年MM月dd日HH时mm分ss秒
     * @return Date
     */
    private static Date getTime(String user_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(user_time);
        } catch (ParseException ex) {
            ex.printStackTrace();
            sdf.applyPattern("yyyy-MM-dd");
            try {
                date = sdf.parse(user_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static String countDownTime(String date) {

        Date outTime = getTime(date);

        if (outTime != null) {
            long time = outTime.getTime();
            if (time < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                time *= 1000;
            }

            long now = System.currentTimeMillis();
            if (time <= now || time <= 0) {
                return null;
            }

            final long diff = time - now;
            long day = diff / DAY;
            long hours = diff % DAY / HOUR;
            long minute = diff % HOUR / MINUTE;
            long second = diff % MINUTE / SECOND;

            if (day > 0) {
                return day + "天";
            } else if (hours > 0) {
                return hours + "小时";
            } else if (minute > 0) {
                return minute + "分钟";
            } else if (second > 0) {
                return second + "秒";
            }

            return day + "天" + hours + "时" + minute + "分" + second + "秒";
        } else {
            return "";
        }
    }


    public static String countDownDays(String date) {

        Date outTime = getTime(date);

        if (outTime != null) {
            long time = outTime.getTime();
            if (time < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                time *= 1000;
            }

            long now = System.currentTimeMillis();
            if (time <= now || time <= 0) {
                return null;
            }
//
//            final long diff = time - now;
//            long day = diff / DAY;
//            long hours = diff % DAY / HOUR;
////            long minute = diff % HOUR / MINUTE;
////            long second = diff % MINUTE / SECOND;
//
//            String dayString = "";
//            if (day > 0) {
//                dayString = day + "天";
//            }
//            if (hours > 0) {
//                dayString = (day + 1) + "天";
//            }
//
//            return dayString;

            int day = differentDays(outTime);
            if (day < 0) {
                return null;
            }

            return day + "天";
        } else {
            return null;
        }
    }


    private static int differentDays(Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) //闰年
                {
                    timeDistance += 366;
                } else //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }
}

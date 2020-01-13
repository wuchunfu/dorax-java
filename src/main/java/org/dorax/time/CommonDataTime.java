package org.dorax.time;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 时间计算工具类
 *
 * @author wuchunfu
 * @date 2020-01-03
 */
public class CommonDataTime {
    /**
     * 将毫秒数转换为：xx天，xx时，xx分，xx秒
     */
    public static String formatDateAgo(long millisecond) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = millisecond / dd;
        long hour = (millisecond - day * dd) / hh;
        long minute = (millisecond - day * dd - hour * hh) / mi;
        long second = (millisecond - day * dd - hour * hh - minute * mi) / ss;
        StringBuilder sb = new StringBuilder();
        if (millisecond >= 0 && millisecond < 1000) {
            sb.append(millisecond).append("毫秒");
        } else {
            if (day > 0) {
                sb.append(day).append("天");
            }
            if (hour > 0) {
                sb.append(hour).append("时");
            }
            if (minute > 0) {
                sb.append(minute).append("分");
            }
            if (second > 0) {
                sb.append(second).append("秒");
            }
        }
        return sb.toString();
    }

    /**
     * 将过去的时间转为为，刚刚，xx秒，xx分钟，xx小时前、xx天前，大于3天的显示日期
     */
    public static String formatTimeAgo(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatTimeAgo(LocalDate.parse(dateTime, dateTimeFormatter).atStartOfDay());
    }

    /**
     * 将过去的时间转为为，刚刚，xx秒，xx分钟，xx小时前、xx天前，大于3天的显示日期
     */
    public static String formatTimeAgo(LocalDateTime dateTime) {
        String interval;
        long nowTime = Instant.now().toEpochMilli();
        long endTime = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        // 得出的时间间隔是毫秒
        long time = nowTime - endTime;
        // 如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
        if (time / 1000 < 10 && time / 1000 >= 0) {
            interval = "刚刚";
            // 如果时间间隔大于24小时则显示多少天前
        } else if (time / 3600000 < 24 * 4 && time / 3600000 >= 24) {
            // 得出的时间间隔的单位是天
            int d = (int) (time / (3600000 * 24));
            interval = d + "天前";
            // 如果时间间隔小于24小时则显示多少小时前
        } else if (time / 3600000 < 24 && time / 3600000 >= 1) {
            // 得出的时间间隔的单位是小时
            int h = (int) (time / 3600000);
            interval = h + "小时前";
            // 如果时间间隔小于60分钟则显示多少分钟前
        } else if (time / 60000 < 60 && time / 60000 >= 1) {
            // 得出的时间间隔的单位是分钟
            int m = (int) ((time % 3600000) / 60000);
            interval = m + "分钟前";
            // 如果时间间隔小于60秒则显示多少秒前
        } else if (time / 1000 < 60 && time / 1000 >= 10) {
            int se = (int) ((time % 60000) / 1000);
            interval = se + "秒前";
            // 大于3天的，则显示正常的时间，但是不显示秒
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            interval = dateTimeFormatter.format(dateTime);
        }
        return interval;
    }

    /**
     * Prints the duration in a human readable format as X days Y hours Z minutes etc.
     *
     * @param uptime the uptime in millis
     * @return the time used for displaying on screen or in logs
     */
    public static String printDuration(double uptime) {
        NumberFormat fmtI = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.ENGLISH));
        NumberFormat fmtD = new DecimalFormat("###,##0.000", new DecimalFormatSymbols(Locale.ENGLISH));
        uptime /= 1000;
        if (uptime < 60) {
            return fmtD.format(uptime) + " seconds";
        }
        uptime /= 60;
        if (uptime < 60) {
            long minutes = (long) uptime;
            return fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
        }
        uptime /= 60;
        if (uptime < 24) {
            long hours = (long) uptime;
            long minutes = (long) ((uptime - hours) * 60);
            String s = fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
            if (minutes != 0) {
                s += " " + fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
            }
            return s;
        }
        uptime /= 24;
        long days = (long) uptime;
        long hours = (long) ((uptime - days) * 24);
        String s = fmtI.format(days) + (days > 1 ? " days" : " day");
        if (hours != 0) {
            s += " " + fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
        }
        return s;
    }

    public static boolean waitOnBoolean(boolean expected, long timeout, CheckMethod check) {
        long timeLeft = timeout;
        long interval = 10;
        while (check.check() != expected && timeLeft > 0) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeLeft -= interval;
        }
        return check.check() == expected;
    }

    public interface CheckMethod {
        boolean check();
    }

    public static void main(String[] args) {
        System.out.println(formatDateAgo(Instant.now().getEpochSecond()));
        System.out.println(formatTimeAgo("2019-12-05"));
        System.out.println(formatTimeAgo(LocalDateTime.now()));
        System.out.println(printDuration(Instant.now().getEpochSecond()));
    }
}

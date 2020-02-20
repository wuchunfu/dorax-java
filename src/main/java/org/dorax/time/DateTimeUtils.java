package org.dorax.time;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wuchunfu
 * @date 2020-01-02
 */
public class DateTimeUtils {
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");
    public static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter SHORT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter LONG_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");

    /**
     * 获得当前日期
     *
     * @return 当前日期
     */
    public static Date getNow() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    /**
     * 获取一天的开始时间，2017,7,22 00:00
     *
     * @param time LocalDateTime 时间
     * @return 一天的开始时间
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 获取一天的结束时间，2017,7,22 23:59:59.999999999
     *
     * @param time LocalDateTime 时间
     * @return 一天的结束时间
     */
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
     *
     * @param time   LocalDateTime 时间
     * @param number 数字
     * @param field  TemporalUnit 值
     * @return 日期加上一个数
     */
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    /**
     * 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
     *
     * @param time   LocalDateTime 时间
     * @param number 数字
     * @param field  TemporalUnit 值
     * @return 日期减去一个数
     */
    public static LocalDateTime minus(LocalDateTime time, long number, TemporalUnit field) {
        return time.minus(number, field);
    }

    /**
     * 获取两个日期的差 field参数为ChronoUnit.*
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param field     单位(年月日时分秒)
     * @return 两个日期的差
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) {
            return period.getYears();
        }
        if (field == ChronoUnit.MONTHS) {
            return period.getYears() * 12 + period.getMonths();
        }
        return field.between(startTime, endTime);
    }

    /**
     * 返回当前的日期
     */
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now(ZoneOffset.of("+8"));
    }

    /**
     * 返回当前时间
     */
    public static LocalTime getCurrentLocalTime() {
        return LocalTime.now(ZoneOffset.of("+8"));
    }

    /**
     * 返回当前日期时间
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now(ZoneOffset.of("+8"));
    }

    /**
     * yyyy-MM-dd
     */
    public static String getCurrentDateStr() {
        return LocalDate.now(ZoneOffset.of("+8")).format(DATE_FORMATTER);
    }

    /**
     * yyMMdd
     */
    public static String getCurrentShortDateStr() {
        return LocalDate.now(ZoneOffset.of("+8")).format(SHORT_DATE_FORMATTER);
    }

    public static String getCurrentMonthStr() {
        return LocalDate.now(ZoneOffset.of("+8")).format(YEAR_MONTH_FORMATTER);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTimeStr() {
        return LocalDateTime.now(ZoneOffset.of("+8")).format(DATETIME_FORMATTER);
    }

    public static String getCurrentLongDateTimeStr() {
        return LocalDateTime.now(ZoneOffset.of("+8")).format(LONG_DATETIME_FORMATTER);
    }

    /**
     * yyMMddHHmmss
     */
    public static String getCurrentShortDateTimeStr() {
        return LocalDateTime.now(ZoneOffset.of("+8")).format(SHORT_DATETIME_FORMATTER);
    }

    /**
     * HHmmss
     */
    public static String getCurrentTimeStr() {
        return LocalTime.now(ZoneOffset.of("+8")).format(TIME_FORMATTER);
    }

    public static String getCurrentDateStr(String pattern) {
        return LocalDate.now(ZoneOffset.of("+8")).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getCurrentDateTimeStr(String pattern) {
        return LocalDateTime.now(ZoneOffset.of("+8")).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getCurrentTimeStr(String pattern) {
        return LocalTime.now(ZoneOffset.of("+8")).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalTime parseLocalTime(String timeStr, String pattern) {
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalDateTime(LocalDateTime datetime, String pattern) {
        return datetime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalTime(LocalTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseLocalDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
    }

    public static LocalDateTime parseLongLocalDateTime(String longDateTimeStr) {
        return LocalDateTime.parse(longDateTimeStr, LONG_DATETIME_FORMATTER);
    }

    public static LocalTime parseLocalTime(String timeStr) {
        return LocalTime.parse(timeStr, TIME_FORMATTER);
    }

    public static String formatLocalDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static String formatLocalDateTime(LocalDateTime datetime) {
        return datetime.format(DATETIME_FORMATTER);
    }

    public static String formatLocalTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }

    /**
     * 日期相隔秒
     */
    public static long periodHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return Duration.between(startDateTime, endDateTime).get(ChronoUnit.SECONDS);
    }

    /**
     * 日期相隔天数
     */
    public static long periodDays(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.DAYS);
    }

    /**
     * 日期相隔周数
     */
    public static long periodWeeks(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.WEEKS);
    }

    /**
     * 日期相隔月数
     */
    public static long periodMonths(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.MONTHS);
    }

    /**
     * 日期相隔年数
     */
    public static long periodYears(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.YEARS);
    }

    /**
     * 是否当天
     */
    public static boolean isToday(LocalDate date) {
        return getCurrentLocalDate().equals(date);
    }

    /**
     * 获取当前毫秒数
     */
    public static Long toEpochMilli(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 判断是否为闰年
     */
    public static boolean isLeapYear(LocalDate localDate) {
        return localDate.isLeapYear();
    }

    /**
     * 将java.util.Date 转换为java8 的java.time.LocalDateTime,默认时区为东8区
     *
     * @param date Date 日期
     * @return LocalDateTime 时间
     */
    public static LocalDateTime dateConvertToLocalDateTime(Date date) {
        return date.toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }

    /**
     * 将java8 的 java.time.LocalDateTime 转换为 java.util.Date，默认时区为东8区
     *
     * @param localDateTime LocalDateTime 时间
     * @return Date 日期
     */
    public static Date localDateTimeConvertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
    }

    /**
     * 毫秒转LocalDateTime
     *
     * @param milliseconds 毫秒值
     * @return LocalDateTime 时间
     */
    public static LocalDateTime millisecondsToDatetime(long milliseconds) {
        Instant instant = Instant.ofEpochMilli(milliseconds);
        return LocalDateTime.ofInstant(instant, ZoneOffset.of("+8"));
    }

    /**
     * 将LocalDataTime转为毫秒数
     *
     * @param localDateTime LocalDateTime 时间
     * @return 毫秒数
     */
    public static long dataTimeToTimestamp(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 把long 转换成 日期 再转换成Date类型
     */
    public static Date transferLongToDate(Long millSec) {
        return new Date(millSec);
    }

    public static Date getDateBefore(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -day);
        return calendar.getTime();
    }

    public static String getDateBeforeByLocalDateTime(int day) {
        return dateConvertToLocalDateTime(getDateBefore(day)).format(DATETIME_FORMATTER);
    }

    public static String getDateBeforeBySimpleDateFormat(int day, String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        calendar.add(Calendar.DATE, -day);
        return sdf.format(calendar.getTime());
    }

    public static String getDateAfterBySimpleDateFormat(int day, String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        calendar.add(Calendar.DATE, day);
        return sdf.format(calendar.getTime());
    }

    public static void main(String[] args) {
        System.out.println(getDateBeforeByLocalDateTime(23));
        System.out.println(millisecondsToDatetime(1563867467000L).format(DATETIME_FORMATTER));
        System.out.println(dataTimeToTimestamp(getCurrentLocalDateTime()));
        System.out.println(DateTimeUtils.getCurrentDateTimeStr("yyyy年MM月dd日 HH:mm:ss"));
        System.out.println(DateTimeUtils.getCurrentDateTimeStr());

        Date date = getNow();
        LocalDateTime now = LocalDateTime.now();
        System.out.println("===" + now);
        System.out.println("===" + date);
        LocalDateTime localDateTime = DateTimeUtils.dateConvertToLocalDateTime(date);
        long localDateTimeSecond = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        long dateSecond = date.toInstant().atOffset(ZoneOffset.of("+8")).toEpochSecond();
        System.out.println("dateSecond:" + dateSecond);
        System.out.println("localDateTimeSecond:" + localDateTimeSecond);

        // 增加二十分钟
        System.out.println(DateTimeUtils.formatLocalDateTime(
                DateTimeUtils.plus(LocalDateTime.now(), 20, ChronoUnit.MINUTES), "yyyy年MM月dd日 HH:mm"));
        // 增加两年
        System.out.println(DateTimeUtils.formatLocalDateTime(DateTimeUtils.plus(LocalDateTime.now(), 2, ChronoUnit.YEARS), "yyyy年MM月dd日 HH:mm"));

        LocalDateTime start = LocalDateTime.of(1993, 10, 13, 11, 11);
        LocalDateTime end = LocalDateTime.of(1994, 11, 13, 13, 13);
        System.out.println("年:" + DateTimeUtils.betweenTwoTime(start, end, ChronoUnit.YEARS));
        System.out.println("月:" + DateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MONTHS));
        System.out.println("日:" + DateTimeUtils.betweenTwoTime(start, end, ChronoUnit.DAYS));
        System.out.println("半日:" + DateTimeUtils.betweenTwoTime(start, end, ChronoUnit.HALF_DAYS));
        System.out.println("小时:" + DateTimeUtils.betweenTwoTime(start, end, ChronoUnit.HOURS));
        System.out.println("分钟:" + DateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MINUTES));
        System.out.println("秒:" + DateTimeUtils.betweenTwoTime(start, end, ChronoUnit.SECONDS));
        System.out.println("毫秒:" + DateTimeUtils.betweenTwoTime(start, end, ChronoUnit.MILLIS));
        // =============================================================================================
        /*
         * 年:1 月:13 日:396 半日:792 小时:9506 分钟:570362 秒:34221720 毫秒:34221720000
         */

    }
}

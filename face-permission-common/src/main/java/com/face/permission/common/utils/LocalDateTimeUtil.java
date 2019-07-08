package com.face.permission.common.utils;

import com.face.permission.common.constants.sys.enums.DateTimeFormatterEnum;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Objects;

/**
 * @author plw on 2018/5/4 上午10:10.
 * @version 1.0
 */
public class LocalDateTimeUtil {

    private static final ZoneId zone = ZoneId.systemDefault();

    public static final LocalTime MAX_TIME = LocalTime.of(23, 59, 59);

    public static String format(LocalDateTime dateTime) {
        return format(dateTime, 5);
    }

    /**
     * 将LocalDateTime转化为字符串格式
     *
     * @param dateTime
     * @param type
     * @return
     */
    public static String format(LocalDateTime dateTime, Integer type) {
        try {
            if (Objects.nonNull(dateTime)) {
                DateTimeFormatterEnum format = DateTimeFormatterEnum.getByType(type);
                return format.getFormatter().format(dateTime);
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 将LocalDateTime转化为字符串格式
     *
     * @param dateTime
     * @return
     */
    public static String format(LocalDateTime dateTime, DateTimeFormatterEnum format) {
        try {
            if (Objects.nonNull(dateTime)) {
                return format.getFormatter().format(dateTime);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static LocalDateTime toLocalDateTime(String date) {
        return toLocalDateTime(date, 5);
    }

    /**
     * 将String转化为LocalDateTime
     *
     * @param date
     * @param type
     * @return
     */
    public static LocalDateTime toLocalDateTime(String date, Integer type) {
        try {
            if (StringUtils.isNotBlank(date)) {
                DateTimeFormatterEnum format = DateTimeFormatterEnum.getByType(type);
                //只有年月的话,需要使用LocalDate去解析
                switch (format) {
                    case FORMAT_YM:
                        date += "01";
                    case FORMAT_YMD:
                        return LocalDateTime.of(LocalDate.parse(date, DateTimeFormatterEnum.FORMAT_YMD.getFormatter()), LocalTime.MIN);
                    case FORMAT_Y_M:
                        date += "-01";
                    case FORMAT_Y_M_D:
                        return LocalDateTime.of(LocalDate.parse(date, DateTimeFormatterEnum.FORMAT_Y_M_D.getFormatter()), LocalTime.MIN);
                }
                return LocalDateTime.parse(date, format.getFormatter());
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 将Date转化为LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (Objects.nonNull(date)) {
            Instant instant = date.toInstant();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
            return localDateTime;
        }
        return null;
    }

    /**
     * 将Long转化为LocalDateTime[只支持10位和13位Long]
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime toLocalDateTime(Long timestamp) {
        if (Objects.nonNull(timestamp)) {
            Integer length = String.valueOf(timestamp).length();
            Instant instant = null;
            if (length == 10) {
                instant = Instant.ofEpochSecond(timestamp);
            } else if (length == 13) {
                instant = Instant.ofEpochMilli(timestamp);
            }
            return Objects.nonNull(instant) ? LocalDateTime.ofInstant(instant, zone) : null;
        }
        return null;
    }

    /**
     * 将LocalDateTime转化为Date
     *
     * @param dateTime
     * @return
     */
    public static Date toDate(LocalDateTime dateTime) {
        if (Objects.nonNull(dateTime)) {
            Instant instant = dateTime.atZone(zone).toInstant();
            return Date.from(instant);
        }
        return null;
    }

    /**
     * 获取该月月初时间
     *
     * @return
     */
    public static LocalDateTime firstDayOfMonth() {
        LocalDate localDate = LocalDate.now();
        return LocalDateTime.of(localDate.with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
    }

    public static LocalDateTime atStartOfDay(LocalDate localDate) {
        if (localDate != null) {
            return localDate.atStartOfDay();
        }
        return null;
    }

    public static LocalDateTime atEndOfDay(LocalDate localDate) {
        if (localDate != null) {
            return localDate.plusDays(1).atStartOfDay().minusSeconds(1);
        }
        return null;
    }

    /**
     * 计算【充值】后结束时间
     * 如果开始时间为00:00:00, 则包含当天
     * 举例：delta=2
     * startTime=5.22 00:00:00,  则endTime=5.23 23:59:58
     * startTime=5.22 00:00:01,  则endTime=5.24 23:59:58
     *
     * @param startTime
     * @param delta
     * @return
     */
    public static LocalDateTime getEndTimeWithCharge(LocalDateTime startTime, Integer delta) {
        if (Objects.isNull(startTime) || Objects.isNull(delta)) {
            return null;
        }
        if (equals(LocalTime.MIN, startTime.toLocalTime())) {
            delta--;
        }
        return LocalDateTime.of(startTime.toLocalDate().plusDays(delta), MAX_TIME);
    }

    /**
     * 计算【升级】后补差天数
     * 如果当前时间为23:59:59, 则不包含当天
     * 举例：endTime=5.24 23:59:59
     * now=5.22 23:59:58,  则delta=3
     * now=5.22 23:59:59,  则delta=2
     *
     * @param endTime
     * @return
     */
    public static Integer getDeltaWithUpgrade(LocalDateTime endTime) {
        if (Objects.isNull(endTime)) {
            return null;
        }
        Integer delta = (int) ChronoUnit.DAYS.between(LocalDate.now(), endTime.toLocalDate());
        if (!equals(MAX_TIME, endTime.toLocalTime())) {
            delta++;
        }
        return delta;
    }

    /**
     * LocalTime只比较时分秒
     *
     * @param o1
     * @param o2
     * @return
     */
    public static boolean equals(LocalTime o1, LocalTime o2) {
        if (o1 == o2) {
            return true;
        }
        return o1.getHour() == o2.getHour() && o1.getMinute() == o2.getMinute() && o1.getSecond() == o2.getSecond();
    }
}

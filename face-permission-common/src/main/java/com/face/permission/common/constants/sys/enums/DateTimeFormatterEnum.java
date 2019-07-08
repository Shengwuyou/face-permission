package com.face.permission.common.constants.sys.enums;

import java.time.format.DateTimeFormatter;

/**
 * @author plw on 2018/5/4 上午10:29.
 * @version 1.0
 */
public enum DateTimeFormatterEnum {

    FORMAT_YM(1, DateTimeFormatter.ofPattern("yyyyMM")),

    FORMAT_Y_M(2, DateTimeFormatter.ofPattern("yyyy-MM")),

    FORMAT_YMD(3, DateTimeFormatter.ofPattern("yyyyMMdd")),

    FORMAT_Y_M_D(4, DateTimeFormatter.ofPattern("yyyy-MM-dd")),

    FORMAT_YMD_HMS(5, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),

    FORMAT_YMDHMS(6, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),

    FORMAT_YMDTHMS(7, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),

    FORMAT_YMD_HM(8, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),

    FORMAT_DHMS(9, DateTimeFormatter.ofPattern("ddHHmmss")),

    FORMAT_YYMMDDHHMMSS(10, DateTimeFormatter.ofPattern("yyMMddHHmmss")),

    FORMAT_YYMMDD(11, DateTimeFormatter.ofPattern("yyMMdd")),

    FORMAT_YMDHMSS(12, DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")),

    FORMAT_YMDHMSS_2(13,DateTimeFormatter.ofPattern("yyyy年MM月dd日HH:mm:ss")),

    FORMAT_MD(14,DateTimeFormatter.ofPattern("MM-dd")),

    FORMAT_YEAR(15,DateTimeFormatter.ofPattern("yyyy"));

    private Integer type;

    private DateTimeFormatter formatter;

    DateTimeFormatterEnum(Integer type, DateTimeFormatter formatter) {
        this.type = type;
        this.formatter = formatter;
    }

    /**
     * 找不到类型则返回[yyyy-MM-dd HH:mm:ss]
     *
     * @param type
     * @return
     */
    public static DateTimeFormatterEnum getByType(Integer type) {
        for (DateTimeFormatterEnum e : values()) {
            if (e.type.equals(type)) {
                return e;
            }
        }
        return FORMAT_YMD_HMS;
    }

    public Integer getType() {
        return type;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}

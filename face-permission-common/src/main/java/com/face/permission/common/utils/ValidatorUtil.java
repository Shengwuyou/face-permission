package com.face.permission.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ValidatorUtil {

    /**
     * 正则表达式：验证手机号
     */
    private static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9])|(19[0-9])|(16[0-9]))\\d{8}$";

    /**
     * 正则表达式：验证邮箱
     */
    private static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证身份证
     */
    private static final String REGEX_ID_CARD = "([0-9]{17}([0-9]|X))|([0-9]{15})";

    /**
     * 手机号码前缀
     */
    /*
     * "134", "135", "136", "137", "138", "139",
     * // 移动 "150", "151", "152",
     * // 移动 "157", "158", "159",
     * // 移动 "182", "183", "184", "187", "188",
     * // 移动 "178", "147",
     * // 移动 "130", "131", "132", "155", "156", "185", "186",
     * // 联通 "176", "145",
     * // 联通 "133", "153", "180", "181", "189", "177",
     * // 电信 "1349",
     * // 卫星通信 "170" // 虚拟运营商
     */
    public static boolean isMobile(String mobile) {
        if (StringUtils.isBlank(mobile) || StringUtils.length(mobile) < 11) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验身份证
     *
     * @param isIdentityCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIdentityCard(String isIdentityCard) {
        if (StringUtils.isBlank(isIdentityCard)) {
            return false;
        }
        return Pattern.matches(REGEX_ID_CARD, isIdentityCard);
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean isBankCard(String cardId) {
        if (StringUtils.isBlank(cardId)) {
            return false;
        }
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        int cardLenth = nonCheckCodeCardId.trim().length();
        if (nonCheckCodeCardId == null || cardLenth == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            throw new IllegalArgumentException("不是银行卡的卡号!");
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }
}

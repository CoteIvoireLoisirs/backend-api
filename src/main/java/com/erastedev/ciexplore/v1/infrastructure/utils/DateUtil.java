/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-14 09:19:57
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-14 09:19:57
 */
package com.erastedev.ciexplore.v1.infrastructure.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    public static Timestamp getCurrentTimestamp() {
        return Timestamp.valueOf(getCurrentDateTime());
    }

    public static LocalDateTime getNexTimeDateTime() {
        return LocalDateTime.now().plus(5, ChronoUnit.YEARS);
    }

    public static LocalDateTime getPreviousDate() {
        return LocalDateTime.now().minus(6, ChronoUnit.YEARS);
    }
}

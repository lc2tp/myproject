package com.shop.spider;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    /**
     * 时间戳转日期
     * @param seconds
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        try {
            if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
                return "";
            }
            if (format == null || format.isEmpty()) {
                format = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(new Date(Long.valueOf(seconds + "000")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "长期有效";
    }
}

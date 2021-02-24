package org.hqu.vibsignal_analysis.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CodeGen {
    public static final String LETTERCHAR = "0123456789";
    public static final int RANDLENGTH = 4;

    public static String genExpId(String expClass){
        String expId = expClass + new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date()) + generateUpperLowerString(RANDLENGTH);
        return expId;
    }

    public static String genDataId(String userId){
        String dataId = userId + new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date()) + generateUpperLowerString(RANDLENGTH);
        return dataId;
    }

    /**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)
     *
     * @param length
     * 随机字符串长度
     * @return 随机字符串
     */
    public static String generateUpperLowerString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(LETTERCHAR.charAt(random.nextInt(LETTERCHAR.length())));
        }
        return sb.toString();
    }
}

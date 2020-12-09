package com.wangms.common.utils;

import org.apache.commons.lang3.RandomUtils;

import java.time.Instant;

/**
 * @author: wangms
 * @date: 2020-12-08 19:23
 */
public class SignUrlUtils {
    /**
     * appkey
     * 与对接系统一致
     */
    private static final String appkeyLoal = "040312388d814aaaafed7ae3213c96eb";

    /**
     * secret
     * 与对接系统一致
     */
    private static final String secretLoal = "0dde25d429bb4e1a8115cf56610c0b69";

    public static String createUrl(String url){
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String random = String.valueOf(RandomUtils.nextInt(0, 999999));
        url = url + "?appkey=" + appkeyLoal + "&timestamp=" + timestamp + "&random=" + random + "&sign=" + Md5Util.GetMD5Code(appkeyLoal + "&" + secretLoal + "&" + timestamp + "&" +random);
        return url;
    }

}

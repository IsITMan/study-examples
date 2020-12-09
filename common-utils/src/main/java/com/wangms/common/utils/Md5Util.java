package com.wangms.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 *
 * @author: wangms
 * @date: 2020-12-09 11:04
 */
public class Md5Util {

    /**
     * 转换字节数组为32位密文
     *
     * @param bByte
     * @return
     */
    private static String byteToString(byte[] bByte) {
        StringBuffer buf = new StringBuffer();
        for (int offset = 0; offset < bByte.length; offset++) {
            int i = bByte[offset];
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }

    /**
     * 获取MD5值
     *
     * @param strObj 要加密的字符串
     * @return
     */
    public static String GetMD5Code(String strObj) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            strObj = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return strObj;
    }
}

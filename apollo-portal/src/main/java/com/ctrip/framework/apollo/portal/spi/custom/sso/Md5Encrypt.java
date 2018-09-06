package com.ctrip.framework.apollo.portal.spi.custom.sso;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密操作
 *
 * @author evilhex.
 * @date 2018/9/5 上午10:09.
 */
public class Md5Encrypt {
    /**
     * Used building output as Hex
     */
    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String md5(String text) {
        return md5(text, "UTF-8");
    }

    public static String md5(String text, String charset) {
        MessageDigest msgDigest = null;

        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("System doesn't support MD5 algorithm.");
        }

        try {
            msgDigest.update(text.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("System doesn't support your  EncodingException.");
        }

        byte[] bytes = msgDigest.digest();

        String md5Str = new String(encodeHex(bytes));

        return md5Str;
    }

    public static String code(String input, int bit) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance(System.getProperty("MD5.algorithm", "MD5"));
            if (bit == 16)
                return bytesToHex(md.digest(input.getBytes("utf-8"))).substring(8, 24);
            return bytesToHex(md.digest(input.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new Exception("Could not found MD5 algorithm.", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        int t;
        for (int i = 0; i < 16; i++) {
            t = bytes[i];
            if (t < 0) {
                t += 256;
            }
            sb.append(DIGITS[(t >>> 4)]);
            sb.append(DIGITS[(t % 16)]);
        }
        return sb.toString();
    }

    public static char[] encodeHex(byte[] data) {

        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }

        return out;
    }
}

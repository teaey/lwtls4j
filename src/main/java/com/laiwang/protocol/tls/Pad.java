package com.laiwang.protocol.tls;

/**
 * @author xiaofei.wxf
 * @date 14-1-16
 */
public class Pad {

    private static final char PADDING = '{';

    public static byte[] pad(final byte[] s, final int bs) {
        final int padCount = (bs - (s.length + 1) % bs);
        final byte[] ret = new byte[1 + s.length + padCount];
        ret[0] = (byte) padCount;
        System.arraycopy(s, 0, ret, 1, s.length);
        for (int i = (1 + s.length); i < ret.length; i++) {
            ret[i] = PADDING;
        }
        return ret;
    }

    public static byte[] unpad(final byte[] c) {
        final int padCount = c[0];
        final byte[] ret = new byte[c.length - padCount - 1];
        System.arraycopy(c, 1, ret, 0, ret.length);
        return ret;
    }
}

package com.laiwang.protocol.tls;

/**
 * @author xiaofei.wxf
 * @date 14-1-16
 */
public class Cmd {
    public static final byte CMD_CLIENT_HELLO = 0x01;
    public static final byte CMD_SERVER_HELLO = 0x02;
    public static final byte CMD_DATA = 0x03;
    public static final byte CMD_KEY_REFRESH = 0x04;
    public static final byte CMD_KEY_UPDATED = 0x05;

    private static final short CMD_CLIENT_HELLO_OFFSET = 0x01 << 12;
    private static final short CMD_SERVER_HELLO_OFFSET = 0x02 << 12;
    private static final short CMD_DATA_OFFSET = 0x03 << 12;
    private static final short CMD_KEY_REFRESH_OFFSET = 0x04 << 12;
    private static final short CMD_KEY_UPDATED_OFFSET = 0x05 << 12;

    private static final short MAX_DATA_LENGTH = (0x01 << 12) - 1;

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(MASK));
    }

    public static final void checkDataLength(final int length) {
        if (length > MAX_DATA_LENGTH) throw new IllegalArgumentException("data length " + length);
    }

    public static final short header(final short offset, final int length) {
        checkDataLength(length);
        return (short) (offset | ((short) length));
    }

    public static final int cmd(final int header) {
        return header >> 12;
    }

    private static final int MASK = 0x0FFF;

    public static final int length(final int header) {
        final int ret = header & MASK;
        if (ret > MAX_DATA_LENGTH) {
            throw new IllegalArgumentException("data length " + ret);
        }
        return ret;
    }
}

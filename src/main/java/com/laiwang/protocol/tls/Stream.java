package com.laiwang.protocol.tls;

import java.nio.ByteBuffer;
import java.security.Key;
import java.util.Arrays;

/**
 * @author xiaofei.wxf
 * @date 14-1-16
 */
public abstract class Stream {
    public void done(Key aesKey, int aesKeyKeySize) {
        this.aesKey = aesKey;
        this.keySize = aesKeyKeySize;
    }

    private Key aesKey;
    private int keySize;

    public byte[] encryptData(final byte[] data, final int start, final int end) throws Exception {
        return SymmetricalCoder.AESCoder.INSTANCE.encrypt(this.aesKey, Pad.pad(Arrays.copyOfRange(data, start, end), this.keySize), start, end);
    }

    public byte[] decryptData(final byte[] data, final int start, final int end) throws Exception {
        return SymmetricalCoder.AESCoder.INSTANCE.encrypt(this.aesKey, Pad.unpad(Arrays.copyOfRange(data, start, end)), start, end);
    }

    public byte[] sendServerHello(final byte[] data) throws Exception {
        final byte[] enData = this.encryptData(data, 0, data.length);
        return ByteBuffer.allocate(enData.length + 2).putShort(Cmd.header(Cmd.CMD_SERVER_HELLO, enData.length)).put(enData).array();
    }

    public byte[] sendClientHello(final byte[] data) throws Exception {
        final byte[] enData = this.encryptData(data, 0, data.length);
        return ByteBuffer.allocate(enData.length + 2).putShort(Cmd.header(Cmd.CMD_CLIENT_HELLO, enData.length)).put(enData).array();
    }

    public byte[] sendData(final byte[] data) throws Exception {
        final byte[] enData = this.encryptData(data, 0, data.length);
        return ByteBuffer.allocate(enData.length + 2).putShort(Cmd.header(Cmd.CMD_DATA, enData.length)).put(enData).array();
    }

    public byte[] recvData(final byte[] enData) throws Exception {
        return decryptData(enData, 0, enData.length);
    }

}

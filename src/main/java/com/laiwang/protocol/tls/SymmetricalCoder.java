package com.laiwang.protocol.tls;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;

/**
 * @author xiaofei.wxf
 * @date 14-1-20
 */
public abstract class SymmetricalCoder {

    public static class AESCoder extends SymmetricalCoder {
        private AESCoder() {

        }

        public static AESCoder INSTANCE = new AESCoder();

        @Override
        public String getAlgorithm() {
            return "AES";
        }
    }

    public abstract String getAlgorithm();

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[]   加密数据
     * @throws Exception
     */
    public byte[] encrypt(Key key, byte[] data, int start, int end) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(getAlgorithm());
        //使用密钥初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //执行操作
        return cipher.doFinal(data, start, end);
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[]   解密数据
     * @throws Exception
     */
    public byte[] decrypt(Key key, byte[] data, int start, int end) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(getAlgorithm());
        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key);
        //执行操作
        return cipher.doFinal(data, start, end);
    }

    /**
     * 初始化128位的密钥
     *
     * @return byte[] 密钥
     * @throws Exception
     */
    public SecretKey getSecretKey() throws Exception {
        return getSecretKey(128);
    }

    /**
     * 初始化密钥
     *
     * @param keysize AES加密数据块和密钥长度可以是128比特、192比特、256比特
     * @return byte[] 密钥
     * @throws Exception
     */
    public SecretKey getSecretKey(int keysize) throws Exception {
        //返回生成指定算法的秘密密钥的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(getAlgorithm());
        //初始化此密钥生成器，使其具有确定的密钥大小
        kg.init(keysize);
        //生成一个密钥
        return kg.generateKey();
    }
}

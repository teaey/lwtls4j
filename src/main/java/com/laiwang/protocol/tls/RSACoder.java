package com.laiwang.protocol.tls;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * @author xiaofei.wxf
 * @date 14-1-20
 */
public class RSACoder {
    private static final String ALGORITHM = "RSA";

    /**
     * 1 去除key的头跟尾 2 base64 decode
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static byte[] processKey(File file) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while (((line = in.readLine()) != null)) {
            if ((line.charAt(0) != '-') && (line.charAt(line.length() - 1) != '-'))
                sb.append(line);
        }

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] ret = decoder.decodeBuffer(sb.toString());
        return ret;
    }

    public static PrivateKey getPrivateKey(byte[] key) throws Exception {

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(key);
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
        return kf.generatePrivate(spec);
    }

    public static PublicKey getPublicKey(byte[] key) throws Exception {

        X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
        return kf.generatePublic(spec);
    }

    /**
     * 公钥/私钥加密
     * @param encryptKey
     * @param data
     * @return
     */
    public static byte[] encrypt(Key encryptKey, byte[] data) {
        try {
            Cipher rsa;
            rsa = Cipher.getInstance(ALGORITHM);
            rsa.init(Cipher.ENCRYPT_MODE, encryptKey);
            return rsa.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 公钥/私钥/解密
     * @param decryptionKey
     * @param buffer
     * @return
     */
    public static byte[] decrypt(Key decryptionKey, byte[] buffer) {
        try {
            Cipher rsa;
            rsa = Cipher.getInstance(ALGORITHM);
            rsa.init(Cipher.DECRYPT_MODE, decryptionKey);
            return rsa.doFinal(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";


    /**
     * 私钥签名
     * @param privateKey
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] sign(PrivateKey privateKey, byte[] data) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * 公钥验证签名
     * @param publicKey
     * @param origin
     * @param data
     * @return
     * @throws Exception
     */
    public static boolean verify(PublicKey publicKey,byte[] origin, byte[] data) throws Exception{
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(origin);
        return signature.verify(data);
    }

    public static void main(String[] args) throws Exception {
        PrivateKey privateKey = getPrivateKey(processKey(new File("D:\\projects\\lwstack\\lwtls4j\\src\\main\\resources\\pkcs8_rsa_priv_key.pem")));
        PublicKey publicKey = getPublicKey(processKey(new File("D:\\projects\\lwstack\\lwtls4j\\src\\main\\resources\\rsa_pub_key.pem")));

        System.out.println(privateKey);
        System.out.println(publicKey);

        byte[] beforeEn = "呵呵呵呵".getBytes("utf8");

        byte[] bs = encrypt(publicKey, beforeEn);

        byte[] bs2 = decrypt(privateKey, bs);

        String afterEn = new String(bs2, "utf-8");

        System.out.println(beforeEn + "\n" + afterEn);


        ///

        byte[] sign = sign(privateKey, beforeEn);
        System.out.println(verify(publicKey, beforeEn, sign));

        //

        byte[] bs3 = encrypt(privateKey, beforeEn);
        byte[] bs4 = decrypt(publicKey, bs3);

        System.out.println(Arrays.equals(bs4, beforeEn));
    }
}

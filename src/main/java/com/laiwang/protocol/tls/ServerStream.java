package com.laiwang.protocol.tls;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;

/**
 * @author xiaofei.wxf
 * @date 14-1-16
 */
public class ServerStream extends Stream {
    private final RSAPrivateKey privKey;
    private final Cipher privCipher;



    public ServerStream(RSAPrivateKey privKey) {
        this.privKey = privKey;
        try {
            this.privCipher = Cipher.getInstance(Constants.HELLO_ALGORITHM);
            this.privCipher.init(Cipher.DECRYPT_MODE, privKey);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("privateKey", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new IllegalArgumentException("privateKey", e);
        }
    }

    public byte[] onClientHello(byte[] enData) throws Exception {
        return null;
    }


}

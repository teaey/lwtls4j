import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author xiaofei.wxf
 * @date 14-1-14
 */
public class OioSecurityConnection implements SecurityConnection {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private Cipher cipher;

    private byte[] sign = "hello".getBytes();
    private byte[] clientPubKey = null;


    @Override
    public SecurityConnection connect(InetSocketAddress address) throws Exception {
        Socket socket = new Socket();
        socket.connect(address, 3000);
        socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        return this;
    }

    @Override
    public SecurityConnection handshake(InputStream serverRsaPub) throws Exception {
        byte[] key = new byte[serverRsaPub.available()];
        serverRsaPub.read(key);

        System.out.println(new String(key));

        byte[] pubbytes=(new BASE64Decoder()).decodeBuffer("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4Gn/D194Ni624gJjSwZR\n" +
                "vu2t2l85S9IYwXfMZHnyJ2vvcIEJ7edrn2zUdi+GucadXEnEYdest0pWulvgacS4\n" +
                "655ClAi0IeGgnpB+SLlS8jhSQVPnWZwNhoJvxU3qzQPzUcKhs4jwymIjxUtn6prb\n" +
                "PxgfF+kfkmNwA/OgqiqN0u3snkH9HLWmQ/JIIZwu5CMe7MovVXa1gKdSk5fn8i6H\n" +
                "aPE4KOsFBz0vbDTzl45CxM2+wpROniwxIj3s29knFRIWMiyLZbtihEs4AFl3Y4aI\n" +
                "pKpSNVz1keq9HzdU4xvU7Ppt0zosEUIf77FBnK5p+Y5lJL4EMfjbRHOzkqdWf2t1\n" +
                "bQIDAQAB");

        RSAPublicKey servPubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubbytes));

        byte[] privbytes = new BASE64Decoder().decodeBuffer("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDgaf8PX3g2Lrbi\n" +
                "AmNLBlG+7a3aXzlL0hjBd8xkefIna+9wgQnt52ufbNR2L4a5xp1cScRh16y3Sla6\n" +
                "W+BpxLjrnkKUCLQh4aCekH5IuVLyOFJBU+dZnA2Ggm/FTerNA/NRwqGziPDKYiPF\n" +
                "S2fqmts/GB8X6R+SY3AD86CqKo3S7eyeQf0ctaZD8kghnC7kIx7syi9VdrWAp1KT\n" +
                "l+fyLodo8Tgo6wUHPS9sNPOXjkLEzb7ClE6eLDEiPezb2ScVEhYyLItlu2KESzgA\n" +
                "WXdjhoikqlI1XPWR6r0fN1TjG9Ts+m3TOiwRQh/vsUGcrmn5jmUkvgQx+NtEc7OS\n" +
                "p1Z/a3VtAgMBAAECggEBAMe/0VX/pmKkBgj6EyOOhmip6pt7Mua4JWzfk0DEArTw\n" +
                "L/o2AX6PBI6tMhfYidUGYr7osjJc5NFpC/VpHkFG50piXeSiMqQNzwgUmTNmMdyE\n" +
                "IUMgycZLTwaxR4eZqSu/Hm9iDMhfVeTuoeRVEDQUjp6Ee/iuenm0pNn/mryckAx0\n" +
                "K/i0A6hHn4R6ny0gHgy7adBc9uC6E5l9/Bq/99nskfjGmt6YAaIDlZ04qfHLzZ2W\n" +
                "72RJ0Gwy8sP+kzKHXLVX2VXsvpGuLS5P3U8dwV0ofGInJkR667/bDaIkmH/K/xyZ\n" +
                "ePrSPYSjtFY2y24zKtyxmQqLQs6ylkF6r5G4MxiXsoECgYEA8fiLyzxK2aSpUk0L\n" +
                "NCEyezORxPsO3BjHWDpr4yqzzcbednmaJ0N+DwYhG0OClUaH2NCDpeVQzUsryeyx\n" +
                "HRAnUuS7UIJxi2JnSo1QGnLuWgdZucKJZIgyODPP1fmXXoJgoGidsHNysOUb15q7\n" +
                "55peTHRkKgsaw8qWpJZjhQN3CtECgYEA7Wzc5CeVqJMO1xggVq4CVOgamminx6AX\n" +
                "/Gqe0dr0LGwQysXIAT3JOMcfwlb8rim4q9qOC3a5iF/zo8j9yjCAWxf7IfxKfG/j\n" +
                "KCVa5lBnwtsf84ePtdGzwWoZkgJij8ipayo57sjxr69DKD7iOSME7F96XXufD0Bs\n" +
                "IyT9zN0u790CgYBnwKAIfAjIO8+Sh5CHQLjJlPorEU901ncbgKlkFZpVYLFg8ZFE\n" +
                "xq7VuFYXv6HNahmzwOzCBIraDCJxibkD8jo6U/NxeQ1Aq+gIUoGHZcKH/eDeQujw\n" +
                "n2mRKb4zTLs3/gV6ooEC+O2zpfmDQEjT7SAD/mA4mtxWGZbwab+weCTiEQKBgQC7\n" +
                "ZCbb9NpMWPVnk2VCxMiWVzNYF64FAzirQ0PjAluiDBEc2gnH4QpGw/MvIhUBKXDW\n" +
                "QwPFzIkXOBRGe0bEJI/KVh0ib8widvTlJMaf7WpYAzcusRoeOUsVJzbTZQqBzbDa\n" +
                "IlPjQbpwHXZ191v6GMe0AI/F5OJf2UsAWhvva8u2CQKBgApX/wrr9vewVWAdMTDw\n" +
                "3Gw0lnGf7bvSC6ZeZKdot7ObKx4NgZsLpfG9QLaIobtZB4/wR2ShoGyoWosri903\n" +
                "nzY0uldc3RSrk7oP28IVAMcKPrn1Gz2IH86SEZDRSN7I5bjVn75JUW3+rSTV7Cks\n" +
                "Q034+1kZM4mt78CI7fZkFTqC");

        RSAPrivateKey servPrivKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privbytes));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, servPubKey);
        this.cipher = cipher;

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        this.clientPubKey = publicKey.getEncoded();

        byte[] enSign = this.cipher.doFinal(this.sign);
        byte[] enPubKey = this.cipher.doFinal(this.clientPubKey);

        this.out.write(new HelloPacket(enSign, enPubKey).toByteArray());

        ReadableByteChannel channel = Channels.newChannel(in);

        ByteBuffer signLength = ByteBuffer.allocate(2);
        channel.read(signLength);
        signLength.rewind();

        ByteBuffer sign = ByteBuffer.allocate(signLength.getShort());
        channel.read(sign);
        sign.rewind();

        System.out.println(new String(sign.array()));

        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(servPubKey);

        signature.update(this.sign);
        System.out.println(signature.verify(sign.array()));
        return this;
    }

    @Override
    public int send(SecurityPacket data) throws Exception {
        byte[] bytes = data.toByteArray();
        byte[] bytes2Send = this.cipher.doFinal(bytes);
        this.out.write(bytes2Send);
        return bytes2Send.length;
    }

    @Override
    public SecurityPacket recv() throws Exception {
        return null;
    }

    public static void main(String[] args) throws Exception {
        System.out.println((byte)0xc6);
//        new OioSecurityConnection().connect(new InetSocketAddress("localhost", 8888)).handshake(new FileInputStream("D:\\projects\\custom_encryption_python\\src\\server_pub.pem"));
        new OioSecurityConnection().connect(new InetSocketAddress("localhost", 8888)).handshake(OioSecurityConnection.class.getResourceAsStream("pub_key.pem"));
    }
}

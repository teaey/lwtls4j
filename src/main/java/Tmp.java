import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author xiaofei.wxf
 * @date 14-1-14
 */
public class Tmp {
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException {
        byte[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        byte[] en = encrypt(data, "111111");
        byte[] de = decrypt(en, "111111");
        System.out.println(Arrays.toString(data));
        System.out.println(Arrays.toString(en));
        System.out.println(Arrays.toString(de));
    }


    /**
     * ����
     *
     * @param content  ���������
     * @param password ������Կ
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// ����������
            cipher.init(Cipher.DECRYPT_MODE, key);// ��ʼ��
            byte[] result = cipher.doFinal(content);
            return result; // ����
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ����
     *
     * @param content  ��Ҫ���ܵ�����
     * @param password ��������
     * @return
     */
    public static byte[] encrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// ����������
            cipher.init(Cipher.ENCRYPT_MODE, key);// ��ʼ��
            byte[] result = cipher.doFinal(content);
            return result; // ����
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

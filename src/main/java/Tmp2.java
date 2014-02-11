import org.apache.commons.codec.DecoderException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaofei.wxf
 * @date 14-1-16
 */
public class Tmp2 {
    public static void main(String[] args) throws DecoderException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        System.out.println(digest.digest(new byte[100000]).length);
    }
}

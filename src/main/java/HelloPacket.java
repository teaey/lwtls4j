import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author xiaofei.wxf
 * @date 14-1-14
 */
public class HelloPacket implements SecurityPacket {
    private byte[] encryptedSign;
    private byte[] encryptedClientPub;

    public HelloPacket(byte[] encryptedSign, byte[] encryptedClientPub) {
        this.encryptedSign = encryptedSign;
        this.encryptedClientPub = encryptedClientPub;
    }

    @Override
    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(2 + 2 + encryptedSign.length + encryptedClientPub.length);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putShort((short) encryptedSign.length);
        buffer.put(encryptedSign);
        buffer.putShort((short) encryptedClientPub.length);
        buffer.put(encryptedClientPub);
        return buffer.array();
    }
}

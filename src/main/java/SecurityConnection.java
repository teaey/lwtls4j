import java.io.InputStream;
import java.net.InetSocketAddress;

/**
 * @author xiaofei.wxf
 * @date 14-1-14
 */
public interface SecurityConnection<T extends SecurityPacket> {

    SecurityConnection connect(InetSocketAddress address) throws Exception;

    SecurityConnection handshake(InputStream serverRsaPub) throws Exception;

    int send(T data) throws Exception;

    T recv() throws Exception;
}

package oneoy.org.netty.handler;

import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.logging.Logger;

/**
 * Explain----> 配置SSL/https
 *
 * @author One oy  欧阳
 * @date 2020/10/9 16:40
 * @Motto 三分孤独, 七分狂傲。
 * Created by One oy on 2020/10/9
 */
public class SSLHandlerProvider {

    private static final String PROTOCOL = "TLS";
    private static SSLContext serverSSLContext = null;
    private static final Logger logger = Logger.getLogger(SSLHandlerProvider.class.getName());

    public static SslHandler getSSLHandler() {
        SSLEngine sslEngine = null;
        if (serverSSLContext == null) {
            logger.severe("Server SSL context is null");
            System.exit(-1);
        }

        sslEngine = serverSSLContext.createSSLEngine();
        sslEngine.setUseClientMode(false); //服务器端模式
        sslEngine.setNeedClientAuth(false);//不需要验证客户端
        return new SslHandler(sslEngine);
    }

    public static boolean initSSLContext(String KEYSTORE, String KEYSTORE_TYPE, String KEYSTORE_PASSWORD, String CERT_PASSWORD) {
        logger.info("Initiating SSL context");
        KeyStore ks = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(KEYSTORE);
            ks = KeyStore.getInstance(KEYSTORE_TYPE);
            ks.load(inputStream, KEYSTORE_PASSWORD.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.severe("Cannot close keystore file stream " + e);
            }
        }
        try {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, CERT_PASSWORD.toCharArray());
            KeyManager[] keyManagers = kmf.getKeyManagers();
            TrustManager[] trustManagers = null;

            serverSSLContext = SSLContext.getInstance(PROTOCOL);
            serverSSLContext.init(keyManagers, trustManagers, null);

        } catch (Exception e) {
            logger.severe("Failed to initialize the server-side SSLContext" + e);
            return false;
        }

        return true;
    }
}

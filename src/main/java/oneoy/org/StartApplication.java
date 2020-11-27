package oneoy.org;

import oneoy.org.netty.ConfigServer;
import oneoy.org.netty.handler.SSLHandlerProvider;
import oneoy.org.util.MyReflectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;

@SpringBootApplication
@ComponentScan(basePackages = "oneoy")
public class StartApplication {

    private static int serverPort;

    //SSL
    private static String KEYSTORE;
    private static boolean ENABLED = false;
    private static String KEYSTORE_TYPE;
    private static String KEYSTORE_PASSWORD;

    public static void main(String[] args) {
        try {
            String config = "config";
            ConfigurableApplicationContext context = SpringApplication.run(StartApplication.class, args);

            DispatcherServlet dispatcherServlet = context.getBean(DispatcherServlet.class);
            MockServletConfig mockServletConfig = new MockServletConfig();
            MyReflectionUtils.setFieldValue(dispatcherServlet, config, mockServletConfig);

            try {
                dispatcherServlet.init();
            } catch (ServletException e) {
                e.printStackTrace();
            }

            //初始化https配置
            boolean isHttps = false;

            if (ENABLED && KEYSTORE != null && KEYSTORE_TYPE != null && KEYSTORE_PASSWORD != null) {
                isHttps = SSLHandlerProvider.initSSLContext(KEYSTORE, KEYSTORE_TYPE, KEYSTORE_PASSWORD, KEYSTORE_PASSWORD);
            }

            new ConfigServer().startServer(serverPort, isHttps);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Value("${server.port:80}")
    private void setServerPort(int port) {
        serverPort = port;
    }

    @Value("${server.ssl.enabled}")
    private void setEnabled(boolean enabled) {
        ENABLED = enabled;
    }

    @Value("${server.ssl.key-store}")
    private void setKeystore(String keystore) {
        KEYSTORE = keystore;
    }

    @Value("${server.ssl.key-store-type}")
    private void setKeystoreType(String keystoreType) {
        KEYSTORE_TYPE = keystoreType;
    }

    @Value("${server.ssl.key-store-password}")
    private void setKeystorePassword(String keystorePassword) {
        KEYSTORE_PASSWORD = keystorePassword;
    }

}

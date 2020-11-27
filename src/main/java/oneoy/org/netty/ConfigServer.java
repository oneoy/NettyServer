package oneoy.org.netty;

import oneoy.org.netty.handler.ActionHandler;
import oneoy.org.netty.handler.DispatcherServletHandler;
import oneoy.org.netty.handler.GenerateServletRequestHandler;
import oneoy.org.netty.handler.SSLHandlerProvider;
import oneoy.org.util.SpringContextUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Explain---->
 *
 * @author One oy  欧阳
 * @date 2020/9/21 14:59
 * @Motto 三分孤独, 七分狂傲。
 * Created by One oy on 2020/9/21
 */
public class ConfigServer {

    private static Logger logger = Logger.getLogger(ConfigServer.class.getName());
    private static boolean isLinux = System.getProperty("os.name").toLowerCase().contains("linux");

    public void startServer(int port, boolean isHttps) {
        long start = System.currentTimeMillis();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);

        NioEventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

        try {
            final ServerBootstrap b = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_REUSEADDR, true);
            b.childOption(ChannelOption.SO_RCVBUF, 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 1024 * 1024 * 10)

                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)

                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    //.handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ActionHandler actionHandler = SpringContextUtils.getApplicationContext().getBean(ActionHandler.class);
                            DispatcherServletHandler dispatcherServletHandler = SpringContextUtils.getApplicationContext().getBean(DispatcherServletHandler.class);

                            ChannelPipeline pipeline = ch.pipeline();

                            if (isHttps) {
                                pipeline.addLast("ssl", SSLHandlerProvider.getSSLHandler());
                            }

                            pipeline
                                    .addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(65536))
                                    .addLast(new ChunkedWriteHandler())
                                    .addLast(new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS))
                                    .addLast(GenerateServletRequestHandler.class.getSimpleName(), new GenerateServletRequestHandler())

                                    .addLast(ActionHandler.class.getSimpleName(), actionHandler)
                                    .addLast("handler", dispatcherServletHandler);
                        }
                    });

            logger.info(String.format("port is %s, Start Time consume %sms", port, System.currentTimeMillis() - start));
            b.bind().sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(0);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

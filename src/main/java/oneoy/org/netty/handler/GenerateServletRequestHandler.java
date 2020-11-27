package oneoy.org.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.DATE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Explain---->
 *
 * @author One oy  欧阳
 * @date 2020/9/21 16:24
 * @Motto 三分孤独, 七分狂傲。
 * Created by One oy on 2020/9/21
 */
public class GenerateServletRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String TEXT_CHARSET = String.format("%s; %s=%s", HttpHeaderValues.TEXT_PLAIN, HttpHeaderValues.CHARSET, CharsetUtil.UTF_8);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        if (!msg.decoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }
        MockHttpServletRequest servletRequest = createServletRequest(msg);

        ctx.fireChannelRead(servletRequest);
    }


    private MockHttpServletRequest createServletRequest(FullHttpRequest fullHttpRequest) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(fullHttpRequest.uri()).build();

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI(uriComponents.getPath());
        servletRequest.setPathInfo(uriComponents.getPath());
        servletRequest.setMethod(fullHttpRequest.method().name());

        if (uriComponents.getScheme() != null) {
            servletRequest.setScheme(uriComponents.getScheme());
        }
        if (uriComponents.getHost() != null) {
            servletRequest.setServerName(uriComponents.getHost());
        }
        if (uriComponents.getPort() != -1) {
            servletRequest.setServerPort(uriComponents.getPort());
        }

        for (String name : fullHttpRequest.headers().names()) {
            servletRequest.addHeader(name, fullHttpRequest.headers().get(name));
        }

        ByteBuf bbContent = fullHttpRequest.content();
        String s = bbContent.toString(CharsetUtil.UTF_8);
        servletRequest.setContent(s.getBytes(CharsetUtil.UTF_8));

        if (uriComponents.getQuery() != null) {
            String query = UriUtils.decode(uriComponents.getQuery(), CharsetUtil.UTF_8);
            servletRequest.setQueryString(query);
        }

        for (Map.Entry<String, List<String>> entry : uriComponents.getQueryParams().entrySet()) {
            for (String value : entry.getValue()) {
                servletRequest.addParameter(
                        UriUtils.decode(entry.getKey(), CharsetUtil.UTF_8),
                        UriUtils.decode(value, CharsetUtil.UTF_8));
            }
        }

        return servletRequest;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        ByteBuf content = Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8);

        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1,
                status,
                content
        );
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, TEXT_CHARSET);
        response.headers().set(DATE, System.currentTimeMillis());

        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    }


    public String getRemoteIP(FullHttpRequest httpRequest, ChannelHandlerContext channelHandlerContext) {
        Channel channel = channelHandlerContext.channel();
        String ip = "";
        try {
            String ipForwarded = httpRequest.headers().get("x-forwarded-for");
            if (StringUtils.isEmpty(ipForwarded) || "unknown".equalsIgnoreCase(ipForwarded)) {
                InetSocketAddress inSocket = (InetSocketAddress) channel.remoteAddress();
                ip = inSocket.getAddress().getHostAddress();
            } else {
                ip = ipForwarded;
            }
        } catch (Exception e) {
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }
}

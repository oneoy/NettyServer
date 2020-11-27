package oneoy.org.netty.handler;

import oneoy.org.filter.RequestResponseWrapper;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.stream.ChunkedStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Explain---->
 *
 * @author One oy  欧阳
 * @date 2020/9/21 15:12
 * @Motto 三分孤独, 七分狂傲。
 * Created by One oy on 2020/9/21
 */
@Component
@Scope(scopeName = "prototype")
public class DispatcherServletHandler extends SimpleChannelInboundHandler<RequestResponseWrapper> {
    @Autowired
    private DispatcherServlet dispatcherServlet;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RequestResponseWrapper requestResponseWrapper) throws Exception {
        MockHttpServletRequest servletRequest = (MockHttpServletRequest) requestResponseWrapper.getServletRequest();
        MockHttpServletResponse servletResponse = (MockHttpServletResponse) requestResponseWrapper.getServletResponse();
        dispatcherServlet.service(servletRequest, servletResponse);

        HttpResponseStatus status = HttpResponseStatus.valueOf(servletResponse.getStatus());
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);

        for (String name : servletResponse.getHeaderNames()) {
            response.headers().add(name, servletResponse.getHeader(name));
        }

        channelHandlerContext.write(response);

        InputStream contentStream = new ByteArrayInputStream(servletResponse.getContentAsByteArray());

        ChunkedStream stream = new ChunkedStream(contentStream);
        ChannelFuture writeFuture = channelHandlerContext.writeAndFlush(stream);
        writeFuture.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.close();
    }
}

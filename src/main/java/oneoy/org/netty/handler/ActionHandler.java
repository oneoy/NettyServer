package oneoy.org.netty.handler;

import oneoy.org.filter.ApplicationFilterChain;
import oneoy.org.filter.ApplicationFilterFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.context.annotation.Scope;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;

/**
 * Explain---->
 *
 * @author One oy  欧阳
 * @date 2020/9/21 15:08
 * @Motto 三分孤独, 七分狂傲。
 * Created by One oy on 2020/9/21
 */
@Component
@Scope(scopeName = "prototype")
public class ActionHandler extends SimpleChannelInboundHandler<MockHttpServletRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MockHttpServletRequest msg) throws Exception {
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
        ApplicationFilterChain filterChain = ApplicationFilterFactory.createFilterChain(ctx, msg);
        if (filterChain == null) {
            return;
        }

        filterChain.doFilter(msg, httpServletResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}

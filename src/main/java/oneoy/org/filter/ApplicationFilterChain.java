package oneoy.org.filter;


import io.netty.channel.ChannelHandlerContext;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Explain -->
 * @Author one oy
 * @Motto 三分孤独, 七分狂傲.
 * @Date 2020/9/21 19:44
 **/
public final class ApplicationFilterChain implements FilterChain {

    private List<ApplicationFilterConfig> filters = new ArrayList<>();

    private int pos = 0;

    private ChannelHandlerContext ctx;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {

        internalDoFilter(request, response);
    }

    private void internalDoFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (pos < filters.size()) {
            ApplicationFilterConfig filterConfig = filters.get(pos);
            pos++;
            Filter filter = null;
            try {
                filter = filterConfig.getFilter();
                filter.doFilter(request, response, this);
            } catch (IOException | ServletException | RuntimeException e) {
                throw e;
            } catch (Throwable e) {
                throw new ServletException();
            }
            return;
        }

        try {
            RequestResponseWrapper wrapper = new RequestResponseWrapper();
            wrapper.setServletRequest(request);
            wrapper.setServletResponse(response);
            ctx.fireChannelRead(wrapper);
        } catch (Throwable e) {
            throw new ServletException();
        } finally {
        }
    }

    void addFilter(ApplicationFilterConfig filterConfig) {
        for (ApplicationFilterConfig filter : filters) {
            if (filter == filterConfig) {
                return;
            }
        }

        filters.add(filterConfig);
    }


    void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}

package oneoy.org.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import java.io.IOException;

/**
 * Explain---->
 *
 * @author One oy  欧阳
 * @date 2020/9/19 17:09
 * @Motto 三分孤独, 七分狂傲。
 * Created by One oy on 2020/9/19
 */
public class DynamicChangePathFilter implements Filter, Ordered {

    private final int order = Ordered.LOWEST_PRECEDENCE - 1;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MockHttpServletRequest mockHttpServletRequest = (MockHttpServletRequest) servletRequest;

        filterChain.doFilter(mockHttpServletRequest, servletResponse);
    }

    @Override
    public int getOrder() {
        return order;
    }
}

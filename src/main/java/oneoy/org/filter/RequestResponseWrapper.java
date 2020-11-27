package oneoy.org.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Explain---->
 *
 * @author One oy  欧阳
 * @date 2020/9/21 15:12
 * @Motto 三分孤独, 七分狂傲。
 * Created by One oy on 2020/9/21
 */
public class RequestResponseWrapper {
    ServletRequest servletRequest;

    ServletResponse servletResponse;

    public ServletRequest getServletRequest() {
        return servletRequest;
    }

    public void setServletRequest(ServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public ServletResponse getServletResponse() {
        return servletResponse;
    }

    public void setServletResponse(ServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }
}

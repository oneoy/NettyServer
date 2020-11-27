package oneoy.org.base.servletconfig;

import org.springframework.mock.web.MockServletContext;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;

/**
 * Explain---->
 *
 * @author One oy  欧阳
 * @date 2020/9/19 17:08
 * @Motto 三分孤独, 七分狂傲。
 * Created by One oy on 2020/9/19
 */
public class MyServletContext extends MockServletContext{

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        return null;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter){
        return null;
    }
}

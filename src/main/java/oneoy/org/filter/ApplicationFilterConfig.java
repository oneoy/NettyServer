package oneoy.org.filter;


import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;


/**
 * @Explain -->
 * @Author one oy
 * @Motto 三分孤独, 七分狂傲.
 * @Date 2020/9/21 19:46
 **/
public final class ApplicationFilterConfig implements FilterConfig {

    private Filter filter;

    private String filterName;

    private String urlPatterns;

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public String getInitParameter(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return null;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public String getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(String urlPatterns) {
        this.urlPatterns = urlPatterns;
    }


}

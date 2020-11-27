package oneoy.org.base;

import oneoy.org.base.servletconfig.MyServletContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.servlet.ServletContext;

/**
 * Explain---->
 *
 * @author One oy  欧阳
 * @date 2020/9/19 17:07
 * @Motto 三分孤独, 七分狂傲。
 * Created by One oy on 2020/9/19
 */
public class SpringListener implements SpringApplicationRunListener {
    public SpringListener(SpringApplication application, String[] args) {
        super();
    }

    @Override
    public void starting() {
        System.out.println("start run listener");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        ServletContext servletContext = new MyServletContext();
        ServletWebServerApplicationContext applicationContext = (ServletWebServerApplicationContext) context;
        applicationContext.setServletContext(servletContext);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context){

    }

    @Override
    public void started(ConfigurableApplicationContext context){

    }

    @Override
    public void running(ConfigurableApplicationContext context){

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception){

    }
}

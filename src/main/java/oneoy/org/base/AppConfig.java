package oneoy.org.base;

import oneoy.org.filter.ApplicationFilterConfig;
import oneoy.org.filter.ApplicationFilterConfigRegistry;
import oneoy.org.filter.DynamicChangePathFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explain---->
 *
 * @author One oy  欧阳
 * @date 2020/9/19 17:08
 * @Motto 三分孤独, 七分狂傲。
 * Created by One oy on 2020/9/19
 */
@Configuration
public class AppConfig {

    @Bean
    public ApplicationFilterConfigRegistry getApplicationFilterConfigRegistry() {
        ApplicationFilterConfigRegistry registry = new ApplicationFilterConfigRegistry();

        ApplicationFilterConfig dynamicChangePathFilter = new ApplicationFilterConfig();
        dynamicChangePathFilter.setFilter(new DynamicChangePathFilter());
        dynamicChangePathFilter.setFilterName(DynamicChangePathFilter.class.getSimpleName());
        dynamicChangePathFilter.setUrlPatterns("/*");

        registry.register(dynamicChangePathFilter);
        return registry;
    }
}

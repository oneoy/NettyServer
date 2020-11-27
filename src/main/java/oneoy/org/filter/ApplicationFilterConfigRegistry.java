package oneoy.org.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Explain -->
 * @Author one oy
 * @Motto 三分孤独, 七分狂傲.
 * @Date 2020/9/21 19:47
 **/
public class ApplicationFilterConfigRegistry {

    private List<ApplicationFilterConfig> applicationFilterConfigs = new ArrayList<>();

    public void register(ApplicationFilterConfig applicationFilterConfig) {
        applicationFilterConfigs.add(applicationFilterConfig);
    }

    public List<ApplicationFilterConfig> getApplicationFilterConfigs() {
        return applicationFilterConfigs;
    }
}

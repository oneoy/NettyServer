/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package oneoy.org.filter;

import oneoy.org.util.SpringContextUtils;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * @Explain -->
 * @Author one oy
 * @Motto 三分孤独, 七分狂傲.
 * @Date 2020/9/21 19:49
 **/
public final class ApplicationFilterFactory {

    private ApplicationFilterFactory() {
    }

    public static ApplicationFilterChain createFilterChain(ChannelHandlerContext ctx, ServletRequest request) {
        String requestPath = ((MockHttpServletRequest) request).getRequestURI();
        ApplicationFilterConfigRegistry registry = SpringContextUtils.getBean(ApplicationFilterConfigRegistry.class);
        List<ApplicationFilterConfig> applicationFilterConfigs = registry.getApplicationFilterConfigs();
        ApplicationFilterChain filterChain = new ApplicationFilterChain();
        for (ApplicationFilterConfig applicationFilterConfig : applicationFilterConfigs) {
            if (matchFiltersURL(applicationFilterConfig.getUrlPatterns(), requestPath)) {
                filterChain.addFilter(applicationFilterConfig);
            }
        }
        filterChain.setCtx(ctx);

        return filterChain;
    }

    private static boolean matchFiltersURL(String testPath, String requestPath) {
        if (testPath == null) return false;

        if (testPath.equals(requestPath)) return true;

        if (testPath.equals("/*")) return true;

        if (testPath.endsWith("/*")) {
            if (testPath.regionMatches(0, requestPath, 0,
                    testPath.length() - 2)) {
                if (requestPath.length() == (testPath.length() - 2)) {
                    return true;
                } else if ('/' == requestPath.charAt(testPath.length() - 2)) {
                    return true;
                }
            }
            return false;
        }

        if (testPath.startsWith("*.")) {
            int slash = requestPath.lastIndexOf('/');
            int period = requestPath.lastIndexOf('.');
            if ((slash >= 0) && (period > slash)
                    && (period != requestPath.length() - 1)
                    && ((requestPath.length() - period)
                    == (testPath.length() - 1))) {
                return (testPath.regionMatches(2, requestPath, period + 1,
                        testPath.length() - 2));
            }
        }

        return false;

    }
}

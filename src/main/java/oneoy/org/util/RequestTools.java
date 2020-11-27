package oneoy.org.util;

import com.alibaba.fastjson.JSONObject;
import io.netty.util.CharsetUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Explain---->HttpServletRequest 转json
 *
 * @author One oy  欧阳
 * @date 2020/9/21 17:52
 * @Motto 三分孤独, 七分狂傲。
 * Created by One oy on 2020/9/21
 */
public class RequestTools {

    public static JSONObject requestToJson(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), CharsetUtil.UTF_8));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
        } catch (Exception e) {
        }
        return jsonObject;
    }
}

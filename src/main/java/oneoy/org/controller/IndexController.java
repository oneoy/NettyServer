package oneoy.org.controller;

import com.alibaba.fastjson.JSONObject;
import oneoy.org.util.RequestTools;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Explain --> 菜鸡全栈
 * @Author one oy
 * @Motto 三分孤独, 七分狂傲.
 * @Date 2020/9/21 19:42
 **/
//@RestController
@Controller
public class IndexController {


    @ResponseBody
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        String uuid = UUID.randomUUID().toString();
        return uuid + System.currentTimeMillis();
    }

    @ResponseBody
    @RequestMapping(value = "/test")
    public JSONObject test(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = RequestTools.requestToJson(request);
        System.out.println(jsonObject);
        return jsonObject;
    }

    @ResponseBody
    @GetMapping(value = "/test1")
    public String test1(String body) {
        System.out.println(body);
        return body;
    }

    @GetMapping(value = "/test3")
    public JSONObject test3(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/test4")
    public String test4(String a) {
        System.out.println(a);
        return "jsonObject";
    }

}

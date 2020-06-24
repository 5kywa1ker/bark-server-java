package cn.skywa1ker.bark.controller;

import cn.skywa1ker.bark.model.ApiResponses;
import cn.skywa1ker.bark.model.DeviceToken;
import cn.skywa1ker.bark.service.PushService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * controller
 * 
 * @author hfb
 * @date 20/6/2
 */
@Validated
@Slf4j
@RequiredArgsConstructor
@Controller
public class PushController {

    private final PushService pushService;


    @ResponseBody
    @RequestMapping(value = "/push/{key}", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResponses<Void> push(@NotBlank(message = "key为空") @PathVariable String key,
                                   @Length(max = 100, message = "title长度0-100")
                                   @RequestParam(required = false, defaultValue = "") String title,
                                   @NotNull(message = "body为空") @RequestParam String body, HttpServletRequest request)
            throws JsonProcessingException {
        Map<String, String> parameterMap = getUrlParameterMap(request, "title", "body");
        pushService.push(key, title, body, parameterMap);
        return ApiResponses.success();
    }

    @ResponseBody
    @RequestMapping(value = "/ping", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResponses<String> ping() {
        return ApiResponses.success("pong");
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResponses<Map<String, String>> register(@RequestParam(required = false) String key,
        @NotBlank(message = "deviceToken为空") @RequestParam(name = "devicetoken") String deviceToken) {
        key = pushService.register(key, deviceToken);
        Map<String, String> result = new HashMap<>(1);
        result.put("key", key);
        return ApiResponses.success(result).setMessage("注册成功");
    }

    @GetMapping("/")
    public String index(Map<String, Object> map) {
        List<DeviceToken> list = pushService.listAllDevices();
        map.put("devices", list);
        return "index";
    }

    private Map<String, String> getUrlParameterMap(HttpServletRequest request, String...ignore) {
        Map<String, String> paramsMap = new HashMap<>(16);
        Enumeration<String> params = request.getParameterNames();
        Set<String> ignoreSet = new HashSet<>(Arrays.asList(ignore));
        while (params.hasMoreElements()) {
            String paramName = params.nextElement().trim();
            if (ignoreSet.contains(paramName)) {
                continue;
            }
            try {
                paramsMap.put(paramName, URLDecoder.decode(request.getParameter(paramName), StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }
        return paramsMap;
    }

}

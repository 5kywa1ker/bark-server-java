package cn.skywa1ker.bark.controller;

import cn.skywa1ker.bark.model.ApiResponses;
import cn.skywa1ker.bark.service.PushService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * controller
 * 
 * @author hfb
 * @date 20/6/2
 */
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
public class PushController {

    private final PushService pushService;

    @RequestMapping(value = "/{key}/{body}", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResponses<Void> push1(@NotBlank(message = "key为空") @PathVariable String key,
        @NotNull(message = "body为空") @PathVariable String body, HttpServletRequest request)
        throws JsonProcessingException {
        Map<String, String> parameterMap = getUrlParameterMap(request);
        pushService.push(key, "", body, parameterMap);
        return ApiResponses.success();
    }

    @RequestMapping(value = "/{key}/{title}/{body}", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResponses<Void> push2(@NotBlank(message = "key为空") @PathVariable String key,
        @NotNull(message = "title为空") @PathVariable String title,
        @NotNull(message = "body为空") @PathVariable String body, HttpServletRequest request)
        throws JsonProcessingException {
        Map<String, String> parameterMap = getUrlParameterMap(request);
        pushService.push(key, title, body, parameterMap);
        return ApiResponses.success();
    }

    @RequestMapping(value = "/ping", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResponses<String> ping() {
        return ApiResponses.success("pong");
    }

    @RequestMapping(value = "/register", method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResponses<Map<String, String>> register(@RequestParam(required = false) String key,
        @NotBlank(message = "deviceToken为空") @RequestParam(name = "devicetoken") String deviceToken) {
        key = pushService.register(key, deviceToken);
        Map<String, String> result = new HashMap<>(1);
        result.put("key", key);
        return ApiResponses.success(result).setMessage("注册成功");
    }

    private Map<String, String> getUrlParameterMap(HttpServletRequest request) {
        Map<String, String> paramsMap = new HashMap<>(16);
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement().trim();
            try {
                paramsMap.put(paramName, URLDecoder.decode(request.getParameter(paramName), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }
        return paramsMap;
    }

}

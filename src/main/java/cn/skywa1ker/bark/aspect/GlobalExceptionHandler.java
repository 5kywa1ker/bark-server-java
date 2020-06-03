package cn.skywa1ker.bark.aspect;

import cn.skywa1ker.bark.model.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 * @author hfb
 * @date 20/6/3
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 默认的异常处理
     *
     * @param req request
     * @param e exception
     * @return ApiResponses
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResponses<Void> defaultErrorHandler(HttpServletRequest req, Exception e) {
        //记录日志
        log.error("{} url:{}", e.getMessage(), req.getRequestURI(), e);
        return ApiResponses.failure(ApiResponses.FAIL, e.getMessage());
    }
}

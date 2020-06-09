package cn.skywa1ker.bark.config;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;

/**
 * 请求中分页参数解析器 使用注解@PageableDefault解析成Pageable对象 参数size=1&page=0
 * 
 * @author hfb
 * @date 20/6/9
 */
@Component
@RequiredArgsConstructor
public class PageArgumentResolver implements HandlerMethodArgumentResolver {

    private final PageableHandlerMethodArgumentResolver resolver;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Pageable.class.equals(methodParameter.getParameterType());
    }

    @Override
    public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    }

}

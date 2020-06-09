package cn.skywa1ker.bark.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

/**
 * web mvc config
 * 
 * @author hfb
 * @date 20/6/9
 */
@Configuration
@RequiredArgsConstructor
public class MyWebMvcConfig implements WebMvcConfigurer {

    private final PageArgumentResolver pageArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(pageArgumentResolver);
    }
}

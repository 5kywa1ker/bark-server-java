package cn.skywa1ker.bark.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * WebSecurityConfig
 * 
 * @author hfb
 * @date 20/6/9
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            // 拦截指定url
            .antMatchers("/message/**").authenticated()
            // 其他都不拦截
            .anyRequest().permitAll()
            // httpBasic认证
            .and().formLogin().and().httpBasic();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 忽略h2 console
        web.ignoring().antMatchers("/h2-console/**");
    }
}

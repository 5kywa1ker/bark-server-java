package cn.skywa1ker.bark.service;


import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author hfb
 * @date 20/1/15
 */
public interface PushService {
    /**
     * 推送消息
     * @param key deviceToken对应的key
     * @param title 标题
     * @param body 内容 
     * @param parameters 其他参数
     * @exception ExecutionException
     * @exception InterruptedException
     */
    void push(String key, String title, String body, Map<String, String> parameters) throws ExecutionException,
            InterruptedException, JsonProcessingException;

    /**
     * 注册
     * @param key key 可为空
     * @param deviceToken deviceToken
     * @return key
     */
    String register(String key, String deviceToken);
}

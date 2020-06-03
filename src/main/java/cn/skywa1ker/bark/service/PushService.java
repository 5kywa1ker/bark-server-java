package cn.skywa1ker.bark.service;

import java.util.List;
import java.util.Map;

import cn.skywa1ker.bark.model.PushMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;

/**
 * @author hfb
 * @date 20/1/15
 */
public interface PushService {
    /**
     * 推送消息
     * 
     * @param key
     *            deviceToken对应的key
     * @param title
     *            标题
     * @param body
     *            内容
     * @param parameters
     *            其他参数
     * @exception JsonProcessingException 序列化parameters成json异常
     */
    void push(String key, String title, String body, Map<String, String> parameters) throws JsonProcessingException;

    /**
     * 注册
     * 
     * @param key
     *            key 可为空
     * @param deviceToken
     *            deviceToken
     * @return key
     */
    String register(String key, String deviceToken);

    /**
     * 根据key查询推送消息历史
     * @param key key
     * @param pageable 分页
     * @return list
     */
    List<PushMessage> pageMessageByKey(String key, Pageable pageable);
}

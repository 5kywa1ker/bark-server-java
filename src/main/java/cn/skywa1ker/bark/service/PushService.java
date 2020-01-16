package cn.skywa1ker.bark.service;


/**
 * @author hfb
 * @date 20/1/15
 */
public interface PushService {

    void push(String deviceToken, String title, String content);
}

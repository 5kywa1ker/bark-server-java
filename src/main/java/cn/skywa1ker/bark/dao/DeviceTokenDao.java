package cn.skywa1ker.bark.dao;

import cn.skywa1ker.bark.model.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.skywa1ker.bark.model.PushMessage;

/**
 * @author hfb
 * @date 20/6/2
 */
public interface DeviceTokenDao extends JpaRepository<DeviceToken, Integer> {
    /**
     * 根据key查询
     * @param key key
     * @return DeviceToken
     */
    DeviceToken findFirstByKey(String key);
}

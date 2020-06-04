package cn.skywa1ker.bark.dao;

import cn.skywa1ker.bark.model.PushMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author hfb
 * @date 20/6/2
 */
public interface PushMessageDao extends JpaRepository<PushMessage, Integer> {
    /**
     * 根据deviceToken
     * @param deviceToken deviceToken
     * @param pageable 分页
     * @return list
     */
    List<PushMessage> findByDeviceTokenOrderByIdDesc(String deviceToken, Pageable pageable);
}

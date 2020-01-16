package cn.skywa1ker.bark.dao;

import cn.skywa1ker.bark.model.PushMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushMessageDao extends JpaRepository<PushMessage, Integer> {
}

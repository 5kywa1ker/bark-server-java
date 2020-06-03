package cn.skywa1ker.bark;

import cn.skywa1ker.bark.dao.PushMessageDao;
import cn.skywa1ker.bark.model.PushMessage;
import cn.skywa1ker.bark.service.PushService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BarkServerJavaApplicationTests {

    @Test
    void contextLoads() {}

    @Autowired
    private PushMessageDao pushMessageDao;
    @Autowired
    private PushService pushService;
    @Test
    void test1() {
    }
}

package cn.skywa1ker.bark;

import cn.skywa1ker.bark.dao.PushMessageDao;
import cn.skywa1ker.bark.model.PushMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BarkServerJavaApplicationTests {

    @Test
    void contextLoads() {}

    @Autowired
    private PushMessageDao pushMessageDao;
    @Test
    void test() {
        pushMessageDao.save(new PushMessage().setTitle("tdfi").setContent("jfdsajj").setDeviceToken("jfklsdajkfla"));
    }
}

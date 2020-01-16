package cn.skywa1ker.bark.service.impl;

import cn.skywa1ker.bark.dao.PushMessageDao;
import cn.skywa1ker.bark.model.PushMessage;
import cn.skywa1ker.bark.service.PushService;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author hfb
 * @date 20/1/15
 */
@Service
@RequiredArgsConstructor
public class PushServiceApnsImpl implements PushService {

    private final PushMessageDao pushMessageDao;

    private ApnsService apnsService;

    @PostConstruct
    private void initApnsService() {
        apnsService = APNS.newService()
                .withCert("cert-20210125.p12", "bp")
                .withSandboxDestination()
                .build();
    }

    @Override
    public void push(String deviceToken, String title, String content) {
        PushMessage message = new PushMessage().setDeviceToken(deviceToken).setTitle(title).setContent(content);
        pushMessageDao.save(message);
    }
}

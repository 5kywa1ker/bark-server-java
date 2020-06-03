package cn.skywa1ker.bark.service.impl;

import cn.skywa1ker.bark.dao.DeviceTokenDao;
import cn.skywa1ker.bark.dao.PushMessageDao;
import cn.skywa1ker.bark.model.DeviceToken;
import cn.skywa1ker.bark.model.PushMessage;
import cn.skywa1ker.bark.service.PushService;
import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Pushy实现
 * 
 * @author hfb
 * @date 20/6/2
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class PushServicePushyImpl implements PushService {

    private ApnsClient apnsClient;
    private final PushMessageDao pushMessageDao;
    private final DeviceTokenDao deviceTokenDao;
    private final ObjectMapper objectMapper;

    @PostConstruct
    private void init() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("cert-20210125.p12");
        apnsClient = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
            .setClientCredentials(inputStream, "bp").build();
    }

    @Override
    public void push(String key, String title, String body, Map<String, String> parameters)
            throws ExecutionException, InterruptedException, JsonProcessingException {
        DeviceToken deviceToken = deviceTokenDao.findFirstByKey(key);
        if (deviceToken == null) {
            throw new RuntimeException("找不到 Key 对应的 DeviceToken, 请确保 Key 正确! Key 可在 App 端注册获得。");
        }
        PushMessage pushMessage = new PushMessage().setDeviceToken(deviceToken.getDeviceToken()).setTitle(title)
                .setBody(body).setParameters(objectMapper.writeValueAsString(parameters));
        pushMessageDao.save(pushMessage);


        ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
        payloadBuilder.setSound("1107").setCategoryName("myNotificationCategory");
        payloadBuilder.setAlertTitle(title).setAlertBody(body);
        // CustomProperty
        parameters.forEach(payloadBuilder::addCustomProperty);
        String payload = payloadBuilder.build();
        String token = TokenUtil.sanitizeTokenString(deviceToken.getDeviceToken());
        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, "me.fin.bark", payload);
        PushNotificationFuture<SimpleApnsPushNotification,
            PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture =
                apnsClient.sendNotification(pushNotification);
        PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse = sendNotificationFuture.get();

        if (pushNotificationResponse.isAccepted()) {
            log.info("Push notification accepted by APNs gateway.");
        } else {
            log.warn("Notification rejected by the APNs gateway:{}", pushNotificationResponse.getRejectionReason());
            if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                log.warn("\t…and the token is invalid as of {}",
                    pushNotificationResponse.getTokenInvalidationTimestamp());
            }
        }
    }

    @Override
    public String register(String key, String deviceToken) {
        if (StringUtils.isEmpty(key)) {
            return saveDeviceToken(deviceToken).getKey();
        }
        DeviceToken oldKey = deviceTokenDao.findFirstByKey(key);
        if (oldKey == null) {
            return saveDeviceToken(deviceToken).getKey();
        }
        oldKey.setDeviceToken(deviceToken).setUpdateTime(LocalDateTime.now());
        return deviceTokenDao.save(oldKey).getKey();
    }

    private DeviceToken saveDeviceToken(String deviceToken) {
        String newKey = UUID.randomUUID().toString().replaceAll("-", "");
        DeviceToken token = new DeviceToken().setDeviceToken(deviceToken).setKey(newKey);
        return deviceTokenDao.save(token);
    }
}

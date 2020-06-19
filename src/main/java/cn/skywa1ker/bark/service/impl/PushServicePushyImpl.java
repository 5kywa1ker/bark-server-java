package cn.skywa1ker.bark.service.impl;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.util.IdUtil;
import cn.skywa1ker.bark.config.ApplicationConfigProperties;
import cn.skywa1ker.bark.dao.DeviceTokenDao;
import cn.skywa1ker.bark.dao.PushMessageDao;
import cn.skywa1ker.bark.model.DeviceToken;
import cn.skywa1ker.bark.model.PushMessage;
import cn.skywa1ker.bark.service.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final ApplicationConfigProperties appConfig;

    @PostConstruct
    private void init() throws IOException {
        InputStream inputStream = loadCert(appConfig.getCertFileName());
        apnsClient = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
            .setClientCredentials(inputStream, appConfig.getCertPwd()).build();
    }

    /**
     * 加载证书文件 优先从config目录加载 其次从classpath加载
     * 
     * @param fileName
     *            文件名
     * @return InputStream
     */
    private InputStream loadCert(String fileName) throws FileNotFoundException {
        File file = new File("config" + File.separator + fileName);
        if (file.exists() && file.isFile()) {
            log.info("cert:{}", file.getAbsolutePath());
            return new FileInputStream(file);
        }
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("证书未找到");
        }
        return inputStream;
    }

    @Override
    public void push(String key, String title, String body, Map<String, String> parameters)
        throws JsonProcessingException {
        DeviceToken deviceToken = deviceTokenDao.findFirstByKey(key);
        if (deviceToken == null) {
            throw new RuntimeException("找不到 Key 对应的 DeviceToken, 请确保 Key 正确! Key 可在 App 端注册获得。");
        }
        // 保存到数据库
        PushMessage pushMessage = new PushMessage().setDeviceToken(deviceToken.getDeviceToken()).setKey(key)
            .setTitle(title).setBody(body).setParameters(objectMapper.writeValueAsString(parameters));
        pushMessageDao.save(pushMessage);

        ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
        payloadBuilder.setSound("default").setCategoryName("myNotificationCategory").setMutableContent(true);
        payloadBuilder.setAlertTitle(title).setAlertBody(body);
        String badge = parameters.get("badge");
        if (!StringUtils.isEmpty(badge)) {
            payloadBuilder.setBadgeNumber(NumberUtils.parseNumber(badge, Integer.class));
        }
        // CustomProperty
        parameters.forEach(payloadBuilder::addCustomProperty);
        String payload = payloadBuilder.build();
        String token = TokenUtil.sanitizeTokenString(deviceToken.getDeviceToken());
        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, "me.fin.bark", payload);
        apnsClient.sendNotification(pushNotification).whenCompleteAsync((response, cause) -> {
            if (response == null) {
                log.warn(cause.getMessage(), cause);
                return;
            }
            if (response.isAccepted()) {
                log.info("Push notification accepted by APNs gateway.");
            } else {
                log.warn("Notification rejected by the APNs gateway:{}", response.getRejectionReason());
                if (response.getTokenInvalidationTimestamp() != null) {
                    log.warn("and the token is invalid as of {}", response.getTokenInvalidationTimestamp());
                }
            }
        });
    }

    @Override
    public String register(String key, String deviceToken) {
        if (StringUtils.isEmpty(key)) {
            return saveDeviceToken(null, deviceToken).getKey();
        }
        DeviceToken oldKey = deviceTokenDao.findFirstByKey(key);
        if (oldKey == null) {
            return saveDeviceToken(key, deviceToken).getKey();
        }
        oldKey.setDeviceToken(deviceToken).setUpdateTime(LocalDateTime.now());
        return deviceTokenDao.save(oldKey).getKey();
    }

    @Override
    public List<PushMessage> pageMessageByKey(String key, Pageable pageable) {
        DeviceToken deviceToken = deviceTokenDao.findFirstByKey(key);
        if (deviceToken == null) {
            throw new RuntimeException("设备不存在");
        }
        return pushMessageDao.findByDeviceTokenOrderByIdDesc(deviceToken.getDeviceToken(), pageable);
    }

    @Override
    public void updateDeviceRemark(String key, String remark) {
        DeviceToken deviceToken = deviceTokenDao.findFirstByKey(key);
        Assert.notNull(deviceToken, "设备不存在 key:" + key);
        deviceToken.setRemark(remark);
        deviceTokenDao.save(deviceToken);
    }

    @Override
    public List<DeviceToken> pageDevices(Pageable page) {
        return deviceTokenDao.findAll(page).getContent();
    }

    private DeviceToken saveDeviceToken(String key, String deviceToken) {
        if (StringUtils.isEmpty(key)) {
            key = IdUtil.objectId();
        }
        DeviceToken token = new DeviceToken().setDeviceToken(deviceToken).setKey(key);
        return deviceTokenDao.save(token);
    }

}

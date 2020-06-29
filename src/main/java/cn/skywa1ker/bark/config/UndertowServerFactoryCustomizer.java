package cn.skywa1ker.bark.config;

import java.io.IOException;

import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

import io.undertow.Undertow;
import io.undertow.connector.ByteBufferPool;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * 设置Undertow服务器 XnioWorker Buffers<br/>
 * <a>https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-programmatic-embedded-container-customization</a>
 * 
 * @author hfb
 * @date 20/6/29
 */
@Slf4j
public class UndertowServerFactoryCustomizer implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {
    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        UndertowDeploymentInfoCustomizer undertowDeploymentInfoCustomizer = deploymentInfo -> {
            WebSocketDeploymentInfo info = (WebSocketDeploymentInfo)deploymentInfo.getServletContextAttributes()
                .get(WebSocketDeploymentInfo.ATTRIBUTE_NAME);
            XnioWorker worker = getXnioWorker();
            ByteBufferPool buffers = new DefaultByteBufferPool(true, 1024, 256, 16);
            info.setWorker(worker);
            info.setBuffers(buffers);
        };
        factory.addDeploymentInfoCustomizers(undertowDeploymentInfoCustomizer);
    }

    /**
     * 获取XnioWorker
     */
    private XnioWorker getXnioWorker() {
        XnioWorker worker = null;
        try {
            worker = Xnio.getInstance(Undertow.class.getClassLoader())
                .createWorker(OptionMap.builder().set(Options.THREAD_DAEMON, true).getMap());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return worker;
    }
}

package cn.skywa1ker.bark.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * jackson 配置
 *
 * @author hfb
 * @date 20/6/3
 */
@Configuration
@ConditionalOnClass(JavaTimeModule.class)
public class JacksonConfig {
    @Bean
    @ConditionalOnProperty("spring.jackson.date-format")
    Jackson2ObjectMapperBuilderCustomizer customizeLocalDateTimeFormat(@Value("${spring.jackson.date-format}") String dateFormat) {
        return jacksonObjectMapperBuilder -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
            jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class,
                    new LocalDateTimeDeserializer(formatter));
        };
    }
}

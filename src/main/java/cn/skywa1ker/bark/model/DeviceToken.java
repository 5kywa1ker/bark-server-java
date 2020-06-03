package cn.skywa1ker.bark.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 保存deviceToken
 * 
 * @author hfb
 * @date 20/6/2
 */
@Entity
@Table(name = "device_token", indexes = {@Index(name = "idx_key", columnList = "key")})
@DynamicInsert
@DynamicUpdate
@Data
@Accessors(chain = true)
public class DeviceToken implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column
    private Integer id;

    @Column(columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime addTime;

    @Column(columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String deviceToken;

}

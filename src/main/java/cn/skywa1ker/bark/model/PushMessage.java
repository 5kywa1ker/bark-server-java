package cn.skywa1ker.bark.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author hfb
 * @date 20/1/15
 */
@Entity
@Table(name = "push_message", indexes = {@Index(name = "idx_device_token", columnList = "deviceToken")})
@DynamicInsert
@DynamicUpdate
@Data
@Accessors(chain = true)
public class PushMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column
    private Integer id;

    @Column(nullable = false)
    private String deviceToken;

    private String title;

    @Column(columnDefinition = "tinytext NULL")
    private String content;

    private String img;

    @Column(columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime addTime;

    @Column(columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;
}

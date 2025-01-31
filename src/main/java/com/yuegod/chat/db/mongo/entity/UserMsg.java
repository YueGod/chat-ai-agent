package com.yuegod.chat.db.mongo.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author YueGod
 * @since 2025/2/1
 */
@Data
@Accessors(chain = true)
@Document("user_msg")
public class UserMsg {

  @Id private String id;

  /** 发送用户 */
  @Field("from_id")
  @Indexed
  private String fromId;

  /** 内容 */
  @Field("content")
  private String content;

  /** 语气风格 */
  @Field("tone")
  private String tone;

  /** 话题 */
  @Field("topic")
  private String topic;

  /** 创建时间 */
  @Field("create_time")
  @Indexed(expireAfterSeconds = 60 * 60 * 24 * 30)
  @CreatedDate
  private LocalDateTime createTime;
}

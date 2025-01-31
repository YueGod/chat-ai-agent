package com.yuegod.chat.db.mongo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author YueGod
 * @since 2025/2/1
 */
@Data
@Accessors(chain = true)
@Document("user_info")
public class UserInfo {

  @Id private String id;

  /** 姓名 */
  @Field("name")
  private String name;

  /** 年龄 */
  @Field("age")
  private Integer age;

  /** 城市 */
  @Field("city")
  private String city;

  /** 职业 */
  @Field("profession")
  private String profession;

  /** 兴趣爱好 */
  @Field("hobbies")
  private String hobbies;

  /** 交友偏好 */
  @Field("preferences")
  private String preferences;
}

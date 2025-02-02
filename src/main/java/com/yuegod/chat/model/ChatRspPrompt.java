package com.yuegod.chat.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.yuegod.chat.db.mongo.entity.UserInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 聊天对话回复结构化prompts
 *
 * <p>"content": "你好，我叫张三", "hasInfo": true, "tone": "友好", "userInfo": { "name": "张三", "age": 18,
 * "city": "北京", "profession": "学生", "hobbies": "看电影", "preferences": "喜欢文艺青年"
 *
 * @author YueGod
 * @since 2025/2/2
 */
@Data
@Accessors(chain = true)
public class ChatRspPrompt {

  private String content;

  @JSONField(defaultValue = "false")
  private boolean hasInfo;

  private String tone;

  private UserInfo userInfo;

}

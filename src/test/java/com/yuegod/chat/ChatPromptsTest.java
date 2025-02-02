package com.yuegod.chat;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author YueGod
 * @since 2025/2/2
 */
@SpringBootTest(classes = Application.class)
public class ChatPromptsTest {

  @Resource private ChatModel chatModel;

  private final SystemMessage systemMessage =
      new SystemMessage(
          """
{
  "user_info": {
    "user_name": "",
    "user_city": "成都",
    "user_profession": "未毕业",
    "user_hobbies": "",
    "user_preferences": "",
    "user_moments": ""
  },
  "conversation_history": [
    {
      "message": "你在哪里工作？",
      "is_personal_info": false
    },
    {
      "message": "我在成都读书，马上毕业了。",
      "is_personal_info": true
    }
  ],
  "current_message": "我在成都读书，马上毕业了。",
  "is_personal_info_in_message": true,
  "response_guidelines": {
    "tone_detection": "正式",
    "topic_relevance": "继续",
    "next_step": "继续了解用户的职业"
  }
}
""");

  @Test
  public void test() {}
}

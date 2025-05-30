package com.yuegod.chat.agent;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

/**
 * 聊天风格语气分析ai智能体
 *
 * @author YueGod
 * @since 2025/2/1
 */
@Service
@RequiredArgsConstructor
public class ChatToneAnalyzeAgent {

  private static final Logger log = LoggerFactory.getLogger(ChatRspAgent.class);

  private final ChatModel chatModel;

  public String request(List<String> messages) {
    log.info("发送消息：{}", messages);
    return chatModel.call(getPrompt(messages));
  }

  private String getPrompt(List<String> messages) {
    return "请分析以下对话的用户语气，返回一个关键词（幽默、热情、正式、敷衍、无特定语气）：\n" + String.join("\n", messages);
  }
}

package com.yuegod.chat.agent;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

/**
 * 聊天主题分析ai智能体
 *
 * @author YueGod
 * @since 2025/2/1
 */
@Service
@RequiredArgsConstructor
public class ChatTopicAnalyzeAgent {

  private static final Logger log = LoggerFactory.getLogger(ChatRspAgent.class);

  private final ChatModel chatModel;

  public boolean request(List<String> message) {
    log.info("发送消息：{}", message);
    String resp = chatModel.call(getPrompt(message));
    return "True".equalsIgnoreCase(resp.trim());
  }

  public String getPrompt(List<String> chatHistory) {
    return "请判断以下对话是否围绕同一主题，返回 True 或 False：\n" + String.join("\n", chatHistory);
  }
}

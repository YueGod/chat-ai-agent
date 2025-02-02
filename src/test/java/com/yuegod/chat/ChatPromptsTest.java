package com.yuegod.chat;

import com.alibaba.fastjson2.JSON;
import com.yuegod.chat.db.mongo.entity.UserInfo;
import com.yuegod.chat.db.mongo.entity.UserMsg;
import com.yuegod.chat.model.ChatRspPrompt;
import jakarta.annotation.Resource;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author YueGod
 * @since 2025/2/2
 */
@SpringBootTest(classes = Application.class)
public class ChatPromptsTest {

  @Resource private ChatModel chatModel;

  @Test
  public void test() {

    // 模拟用户信息
    UserInfo userInfo =
        new UserInfo()
            .setName("未知")
            .setCity("未知")
            .setProfession("未知")
            .setHobbies("未知")
            .setPreferences("未知");

    // 模拟对话历史
    List<UserMsg> conversationHistory = Collections.emptyList();

    // 模拟消息
    String currentMessage = "你好";
    // 生成结构化的 Prompt
    ChatRspPrompt prompt = generatePrompt(userInfo, conversationHistory, currentMessage);
    SystemMessage systemMessage = new SystemMessage(JSON.toJSONString(prompt));
    // 调用 ChatModel 生成回复
    String resp = chatModel.call(systemMessage, new UserMessage(currentMessage));

    // 打印回复
    System.out.println(resp);
  }

  // 生成结构化的 Prompt
  public ChatRspPrompt generatePrompt(
      UserInfo userInfo, List<UserMsg> conversationHistory, String currentMessage) {
    // 判断当前消息是否包含个人信息
    boolean isPersonalInfoInMessage = containsPersonalInfo(currentMessage);

    // 创建响应指导
    ChatRspPrompt.ResponseGuidelines responseGuidelines =
        new ChatRspPrompt.ResponseGuidelines()
            .setToneDetection("幽默") // 可以根据对话内容自动分析用户的语气
            .setTopicRelevance("继续") // 判断当前话题是否相关
            .setNextStep("继续了解用户职业"); // 继续提问用户职业

    // 创建对话策略
    ChatRspPrompt.DialogStrategy dialogStrategy =
        new ChatRspPrompt.DialogStrategy()
            .setMaintainConversationRhythm(
                new ChatRspPrompt.MaintainConversationRhythm()
                    .setLongReply("如果回复较长，保持完整但不要过长")
                    .setShortReply("如果回复较短，简短回应"))
            .setTopicManagement(
                new ChatRspPrompt.TopicManagement()
                    .setSameTopic("如果同一话题回复变短，则换话题")
                    .setContinueTopic("如果用户主动提问，继续同一话题"))
            .setToneImitation(
                new ChatRspPrompt.ToneImitation()
                    .setHumorous("幽默语气，回应轻松")
                    .setFormal("正式语气，回应尊重")
                    .setIndifferent("敷衍语气，简短回应"));

    // 创建并返回结构化的 Prompt
    return new ChatRspPrompt()
        .setUserInfo(userInfo)
        .setConversationHistory(conversationHistory)
        .setCurrentMessage(currentMessage)
        .setResponseGuidelines(responseGuidelines)
        .setDialogStrategy(dialogStrategy);
  }

  // 判断消息是否包含个人信息（根据简单规则或者正则）
  private boolean containsPersonalInfo(String message) {
    return message.contains("我在") || message.contains("我叫"); // 示例判断，可以更复杂
  }
}

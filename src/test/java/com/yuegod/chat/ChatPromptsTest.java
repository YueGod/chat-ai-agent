package com.yuegod.chat;

import com.yuegod.chat.agent.ChatRspAgent;
import com.yuegod.chat.db.mongo.entity.UserInfo;
import com.yuegod.chat.db.mongo.entity.UserMsg;
import com.yuegod.chat.model.ChatRspPrompt;
import jakarta.annotation.Resource;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author YueGod
 * @since 2025/2/2
 */
@SpringBootTest(classes = Application.class)
@Slf4j
public class ChatPromptsTest {

  @Resource private ChatModel chatModel;

  @Resource private ChatRspAgent chatRspAgent;

  @Test
  public void test2() {
    // 模拟用户信息
    UserInfo userInfo =
        new UserInfo()
            .setName("未知")
            .setCity("未知")
            .setProfession("未知")
            .setHobbies("未知")
            .setPreferences("未知");
    // 模拟对话历史
    List<UserMsg> conversationHistory =
        List.of(
            new UserMsg().setTone("友好").setContent("hi"),
            new UserMsg().setTone("友好").setContent("耍手机"),
            new UserMsg().setTone("友好").setContent("抖音"));
    List<UserMsg> conversationHistoryReply =
        List.of(
            new UserMsg().setContent("在干什么呢？"),
            new UserMsg().setContent("手机上有在玩什么好玩的游戏或者看什么有趣的东西吗？"),
            new UserMsg().setContent("抖音上最近有追什么热门的挑战或者流行的视频吗？"));
    // 模拟消息
    UserMsg currentMessage = UserMsg.empty(userInfo.getId()).setContent("没有");
    String prompts =
        chatRspAgent.getPrompts(
            userInfo, currentMessage, conversationHistory, conversationHistoryReply, true);
    System.out.println(prompts);
    ChatRspPrompt resp = chatRspAgent.request(prompts, currentMessage.getContent());
    System.out.println(resp);
  }

}

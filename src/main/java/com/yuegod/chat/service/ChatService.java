package com.yuegod.chat.service;

import com.yuegod.chat.agent.ChatRspAgent;
import com.yuegod.chat.agent.ChatToneAnalyzeAgent;
import com.yuegod.chat.agent.ChatTopicAnalyzeAgent;
import com.yuegod.chat.db.mongo.entity.UserInfo;
import com.yuegod.chat.db.mongo.entity.UserMsg;
import com.yuegod.chat.domain.resp.CreateChatResp;
import com.yuegod.chat.model.ChatRspPrompt;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author YueGod
 * @since 2025/2/2
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

  private final ChatRspAgent chatRspAgent;

  private final ChatToneAnalyzeAgent chatToneAnalyzeAgent;

  private final ChatTopicAnalyzeAgent chatTopicAnalyzeAgent;

  private final UserInfoService userInfoService;

  private final UserMsgService userMsgService;

  /** 新创建一个聊天 */
  public CreateChatResp createChat(String userFlag) {
    // 获取用户信息
    UserInfo userInfo = userInfoService.createUser(userFlag);
    // 获取用户最近的消息
    UserMsg lastMsg = UserMsg.empty(userInfo.getId());
    // 获取用户的聊天记录
    List<UserMsg> chatHistory = Collections.emptyList();
    // 获取当前对话是否围绕同一主题
    boolean topic = false;
    // 获取回复
    String prompts = chatRspAgent.getPrompts(userInfo, lastMsg, chatHistory, chatHistory, topic);
    ChatRspPrompt resp = chatRspAgent.request(prompts, "hi");
    log.info("回复：{}", resp);
    return new CreateChatResp().setUserId(userInfo.getId()).setContent(resp.getContent());
  }

  /** 用户发送消息 */
  public ChatRspPrompt sendMsg(String userFlag, String message) {
    // 获取用户信息
    UserInfo userInfo = userInfoService.getUserInfo(userFlag);
    // 获取用户最近的消息
    UserMsg lastMsg = new UserMsg().setContent(message);
    // 获取用户的聊天记录
    List<UserMsg> chatHistory = userMsgService.getChatHistory(userFlag);
    // 我回复的聊天记录
    List<UserMsg> chatHistoryReply = userMsgService.getChatHistoryReply(userFlag);
    // 获取用户语气
    String chatTone =
        chatToneAnalyzeAgent.request(chatHistory.stream().map(UserMsg::getContent).toList());
    lastMsg.setTone(chatTone);
    // 获取当前对话是否围绕同一主题
    boolean topic =
        chatTopicAnalyzeAgent.request(chatHistory.stream().map(UserMsg::getContent).toList());
    // 获取回复
    String prompts =
        chatRspAgent.getPrompts(userInfo, lastMsg, chatHistory, chatHistoryReply, topic);
    ChatRspPrompt resp = chatRspAgent.request(prompts, message);
    log.info("回复：{}", resp);
    // 保存用户聊天信息
    userMsgService.save(userInfo, message);
    return resp;
  }
}

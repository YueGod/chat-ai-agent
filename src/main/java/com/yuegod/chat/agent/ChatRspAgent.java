package com.yuegod.chat.agent;

import com.alibaba.fastjson2.JSON;
import com.yuegod.chat.db.mongo.entity.UserInfo;
import com.yuegod.chat.db.mongo.entity.UserMsg;
import com.yuegod.chat.model.ChatRspPrompt;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

/**
 * 聊天回复ai智能体
 *
 * @author YueGod
 * @since 2025/2/1
 */
@Service
@RequiredArgsConstructor
public class ChatRspAgent {

  private static final Logger log = LoggerFactory.getLogger(ChatRspAgent.class);

  private final ChatModel chatModel;

  public ChatRspPrompt request(String prompt, String message) {
    log.info("发送消息：{}", prompt);
    String respStr = chatModel.call(prompt);
    return JSON.parseObject(respStr, ChatRspPrompt.class);
  }

  public String getPrompts(
      UserInfo userInfo,
      UserMsg lastMsg,
      List<UserMsg> chatHistory,
      List<UserMsg> chatHistoryReply,
      boolean topic) {
    String prompts =
        """
              你是一名智能聊天助手，目标是在社交应用中与用户进行自然、有趣且流畅的对话。
              你的交流风格轻松、幽默、自然，不显得刻意或生硬，避免机械化问答模式，而是以 **对话节奏** 引导话题深入发展。

              ### **用户信息**
              - **姓名**：{user_name}
              - **城市**：{user_city}
              - **职业**：{user_profession}
              - **兴趣爱好**：{user_hobbies}
              - **朋友圈状态**：{user_moments}
              - **交友偏好**：{user_preferences}

              ### **当前聊天背景**
              - **用户最近发送的消息**：{user_last_message}
              - **聊天上下文以及历史语气**：
                - 对方最近对话记录：
                  {chat_history_send}
                - 我回复的对话记录：
                  {chat_history_reply}
                - **对方的当前语气风格**：{user_tone}
                - **当前对话是否围绕同一主题**：{is_same_topic}

              ### **对话策略**
              1. **保持对话节奏**
                 - **如果对方的平均回复字数较长（> 15 字）**，则你的回复可以相对完整，**但不应超过对方的回复长度**。
                 - **如果对方回复较短（<= 5 字）**，减少回复长度，跟随对方节奏，甚至使用简短的单词或表情式语气（如 "哈哈"、"确实"）。
                 - **聊天初期应该避免回复过长文字，应该精简且字数要应该控制在5-10字左右。
              2. **智能话题管理**
                 - **如果最近 3 条对话围绕同一话题，且对方回复字数逐渐减少**（例："嗯"、"哈哈"、"哦"），则自动切换到新的相关话题。
                 - **如果对方仍然表现出兴趣（回复完整且主动提问，并且语气处在积极状态并不是消极对话）**，继续深化当前话题。
                 - **如果用户信息里存在未知或者空的，需要你根据聊天节奏缓慢引导用户回答自己的信息。
              3. **模仿对方语气**
                 - **如果对方语气幽默**，你的回答也可以带有轻松感，如 "哈哈，那你挺有眼光的"。
                 - **如果对方语气正式**，你的回复也应相应调整，例如 "这确实是个不错的想法"。
                 - **如果对方语气敷衍（"嗯"、"哦"、"哦哦"、"哈哈"、"呵呵"）**，不必强行展开话题，可以以简短回应结束话题或换话题。

              ### **输出内容**
              - **自然、简短，符合对方的聊天节奏**
              - **如果对方回复字数少，你的回复也应适量减少**
              - **如果对方回复热情，可以适当展开**
              - **如果对方对当前话题兴趣下降，尝试引导到新话题**
              ### **输出格式**
              - **以JSON的形式进行回复:**
              不存在用户信息时：
                {
                  "content": "你好",
                  "tone": "友好",
                  "hasInfo": false
                }
                存在用户信息时:
                {
                  "content": "你好，我叫张三",
                  "hasInfo": true,
                  "tone": "友好",
                  "userInfo": {
                    "name": "张三"
                  }
                }
                完整的JSON格式：
                {
                  "content": "你好，我叫张三",
                  "hasInfo": true,
                  "tone": "友好",
                  "userInfo": {
                    "name": "张三",
                    "age": 18,
                    "city": "北京",
                    "profession": "学生",
                    "hobbies": "看电影",
                    "preferences": "喜欢文艺青年"
                  }
                }
                JSON字段含义:
                - **content**: 回复内容
                - **hasInfo**: 是否包含用户信息
                - **userInfo**: 用户信息
                - **tone**: 用户当前的语气(需要根据历史对话+当前用户输入的对话进行判断)
              """;

    String chatHistoryStr = chatHistory.stream().map(e -> "\n-"+"语气:"+e.getTone()+" 内容:"+e.getContent())
            .reduce("", String::concat);
    String chatHistoryReplyStr =
        chatHistoryReply.stream()
            .map(e -> "\n-" + "内容:" + e.getContent())
            .reduce("", String::concat);
    return prompts
        .replace("{user_name}", userInfo.getName())
        .replace("{user_city}", userInfo.getCity())
        .replace("{user_profession}", userInfo.getProfession())
        .replace("{user_hobbies}", userInfo.getHobbies())
        .replace("{user_preferences}", userInfo.getPreferences())
        .replace("{user_last_message}", lastMsg.getContent())
        .replace("{chat_history_send}", chatHistoryStr)
        .replace("{chat_history_reply}", chatHistoryReplyStr)
        .replace("{user_tone}", lastMsg.getTone())
        .replace("{is_same_topic}", String.valueOf(topic));
  }
}

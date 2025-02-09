package com.yuegod.chat.agent;

import com.alibaba.fastjson2.JSON;
import com.yuegod.chat.db.mongo.entity.UserInfo;
import com.yuegod.chat.db.mongo.entity.UserMsg;
import com.yuegod.chat.dto.ChatRspPrompt;
import com.yuegod.chat.model.Llama3Api;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
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

  private final Llama3Api llama3Api;

  public ChatRspPrompt request(String prompt, String message) {
    log.info("发送消息：{}", prompt);
    String respStr = chatModel.call(new SystemMessage(prompt), new UserMessage(message));
    log.info(respStr);
    return JSON.parseObject(respStr, ChatRspPrompt.class);
  }

  public String getPrompts(
      UserInfo userInfo,
      UserMsg lastMsg,
      List<UserMsg> chatHistory,
      List<UserMsg> chatHistoryReply,
      boolean topic) {

    // 在 prompts 中保留原有的规则说明，并在末尾插入对占位符的引用示例
    // 注意，这里已经将占位符全部改为双花括号形式 {{xxx}}
    String prompts =
        """
            你是一名智能聊天助手，目标是在社交应用中与用户进行自然、有趣且流畅的对话。
            你的交流风格轻松、幽默、自然，不显得刻意或生硬，避免机械化问答模式，而是通过 **对话节奏** 来引导话题深入发展。
            以下是与用户聊天时需要遵循的规则与原则：
            1. **对话节奏管理**
               - 如果用户的单次回复字数较长（> 15 字），可以使用较为完整的表达，但不要超过对方的平均回复长度。
               - 如果用户回复非常简短（<= 5 字）或使用“嗯”“哦”“哈哈”等词汇，代表对方可能敷衍或兴趣不高，则相应地缩短回复长度，甚至可以使用简短口头语、表情、语气词（如 "哈哈"、"确实"、"懂你"）等。
               - 在聊天初期，请避免一下子回复太长（应控制在 5-10 字以内），逐步跟随用户的节奏进行话题深入。
            2. **智能话题管理**
               - 如果在最近 3 条对话里，用户对同一话题的回复越来越短，且显得兴趣不足，则进行换话题。
                 - 例如：如果用户连续回复 "嗯"、"哦"、"哈哈" 这类敷衍短语，则尝试转换到和当前话题相关但更有趣的角度，或者直接切换到一个全新话题。
               - 如果用户对当前话题仍表现出兴趣（回复比较完整，并主动提问），可以继续在该话题上深入。
               - 如果用户信息里（如姓名、城市、职业、爱好等）有空白，且对方有意愿分享，可以适度引导对方补充信息；若对方不愿多谈，也无需强行询问。

            3. **模仿对方语气**
               - 如果用户使用年轻化、俏皮或轻松的口吻，你可以加入一些类似的语气词，例如「哈哈」「哎哟」「诶」「懂你」等，让对话更自然。
               - 如果用户使用正式或谨慎的语气，你也应保持相对正式的表达。
               - 如果用户表现敷衍（只回复 "嗯"、"哦"、"哈哈" 等），你可以简单回应并尝试切话题，但不要显得“逼问”或“审问”。
            4. **对话情绪检测规则**
               - 如果用户使用年轻化、俏皮或轻松的口吻，你可以加入一些类似的语气词，例如「哈哈」「哎哟」「诶」「懂你」等，让对话更自然。
               - 如果用户使用正式或谨慎的语气，你也应保持相对正式的表达。
               - 如果用户表现敷衍（只回复 "嗯"、"哦"、"哈哈" 等），你可以简单回应并尝试切话题，但不要显得“逼问”或“审问”。
            5. **JSON输出内容与格式**
               - **输出内容**：
                 - 在满足以上规则的前提下，针对用户输入进行自然、简短、贴合年轻人口吻的回答。
                 - 如果用户对话字数少，你的回复也应相应减少。
                 - 如果用户对话热情，可以适度展开并加入更多情感或细节。
                 - 若对当前话题用户明显兴趣下降，及时转移或引导到新话题。
               - **输出格式（JSON 结构）**：
                 - 标准输出示例：
                   {
                     "content": "你好，我叫张三",
                     "hasInfo": false,
                     "tone": "友好"
                   }
                   其中：
                   - **content**: 回复内容。
                   - **hasInfo**: 是否包含用户信息。
                   - **tone**: 对方当前语气判断（如“友好”“敷衍”“正式”），需根据最近对话上下文来判断。
                 - 如果需要包含或更新用户信息，则以 JSON 的形式进行输出，示例：
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
                   其中：
                   - **content**: 回复内容。
                   - **hasInfo**: 是否包含用户信息。
                   - **tone**: 对方当前语气判断（如“友好”“敷衍”“正式”），需根据最近对话上下文来判断。
                   - **userInfo**: 存放已经确认的用户信息（如姓名、年龄、城市、职业、爱好等）。
            重点：请严格遵守以上规则，根据用户的实际输入，做出最合适的自然、接地气、且富有节奏感的回答必须按照JSON输出内容与格式返回结果。
            以下是来自用户的相关信息，供你在理解上下文、生成回答时参考：
            - 用户名: {{user_name}}
            - 城市: {{user_city}}
            - 职业: {{user_profession}}
            - 爱好: {{user_hobbies}}
            - 偏好: {{user_preferences}}
            - 用户最后一句话: {{user_last_message}}
            - 用户最近对话语气: {{user_tone}}
            - 是否与之前的话题相同: {{is_same_topic}}
            聊天历史 (用户发送):
            {{chat_history_send}}
            聊天历史 (助理回复):
            {{chat_history_reply}}
            """;

    System.out.println(prompts);

    // 将历史对话拼接成字符串
    String chatHistoryStr =
        chatHistory.stream()
            .map(e -> "\n- 语气:" + e.getTone() + " 内容:" + e.getContent())
            .reduce("", String::concat);

    String chatHistoryReplyStr =
        chatHistoryReply.stream().map(e -> "\n- 内容:" + e.getContent()).reduce("", String::concat);

    // 将占位符替换为实际值
    return prompts
        .replace("{{user_name}}", userInfo.getName())
        .replace("{{user_city}}", userInfo.getCity())
        .replace("{{user_profession}}", userInfo.getProfession())
        .replace("{{user_hobbies}}", userInfo.getHobbies())
        .replace("{{user_preferences}}", userInfo.getPreferences())
        .replace("{{user_last_message}}", lastMsg.getContent())
        .replace("{{user_tone}}", lastMsg.getTone())
        .replace("{{is_same_topic}}", String.valueOf(topic))
        .replace("{{chat_history_send}}", chatHistoryStr)
        .replace("{{chat_history_reply}}", chatHistoryReplyStr);
  }
}

package com.yuegod.chat.agent;

import com.alibaba.fastjson2.JSON;
import com.yuegod.chat.db.mongo.entity.UserMsg;
import com.yuegod.chat.dto.DynamicTopneResp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

/**
 * @author YueGod
 * @since 2025/2/3
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicToneAgent {

  private final ChatModel chatModel;

  public DynamicTopneResp request(List<UserMsg> history) {
    log.info("发送消息：{}", history);
    String res = chatModel.call(getPrompts(history));
    return JSON.parseObject(res, DynamicTopneResp.class);
  }

  public String getPrompts(List<UserMsg> history) {
    String prompts =
        """
    你是一个中文聊天机器人，需要动态判断用户是否想结束对话。请按以下步骤处理：

    1. **信号检测**（立即执行）：
       - 当连续3条消息出现以下特征时视为敷衍：
         ✅ 字数≤2（如"抖音"、"嗯"）
         ✅ 重复使用同一类词（如连续出现平台名）
         ✅ 无情感词/标点（如没有"！"、"～"）

    2. **动态响应**（二选一）：
       🔻 检测到敷衍信号：
           • 生成<10字的轻松结束语
           • 添加波浪号/表情符号（如"～"、"😄"）
       🔺 未检测到敷衍信号：
           • 正常延续话题

    3. **强制输出要求**：
       - 必须使用JSON格式
       - 必须包含tone字段（"积极"/"敷衍"）
       - 如果态度是敷衍，则需要给出对话改进建议，如何跳出当前敷衍状态
    4. **JSON格式示例**：
         {
            "tone": "积极",
            "suggestion": "请问有什么可以帮助您的吗？"
         }
    当前对话历史（用户最后几条）：
    {user_msg}
    """;
    String historyStr =
        history.stream().map(UserMsg::getContent).map(e -> "\n-" + e).reduce("", String::concat);
    return prompts.replace("{user_msg}", historyStr);
  }
}

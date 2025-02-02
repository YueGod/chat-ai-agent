package com.yuegod.chat.model;

import com.yuegod.chat.db.mongo.entity.UserInfo;
import com.yuegod.chat.db.mongo.entity.UserMsg;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 聊天对话回复结构化prompts
 *
 * @author YueGod
 * @since 2025/2/2
 */
@Data
@Accessors(chain = true)
public class ChatRspPrompt {
  /** 用户信息 */
  private UserInfo userInfo;

  /** 对话历史 */
  private List<UserMsg> conversationHistory;

  /** 当前用户消息 */
  private String currentMessage;

  /** 当前消息是否包含个人信息 */
  private boolean isPersonalInfoInMessage;

  /** 响应指导 */
  private ResponseGuidelines responseGuidelines;

  /** 对话策略 */
  private DialogStrategy dialogStrategy;

  @Data
  @Accessors(chain = true)
  public static class ResponseGuidelines {
    /** 语气检测（例如：幽默、正式） */
    private String toneDetection;

    /** 话题相关性（例如：继续、换话题） */
    private String topicRelevance;

    /** 下一步操作（例如：继续同一话题或换话题） */
    private String nextStep;
  }

  @Data
  @Accessors(chain = true)
  public static class DialogStrategy {
    /** 维持对话节奏策略 */
    private MaintainConversationRhythm maintainConversationRhythm;

    /** 话题管理策略 */
    private TopicManagement topicManagement;

    /** 语气模仿策略 */
    private ToneImitation toneImitation;
  }

  @Data
  @Accessors(chain = true)
  public static class MaintainConversationRhythm {
    /** 如果对方回复较长，如何回应（比如：“可以保持完整，但不要超过对方长度”） */
    private String longReply;

    /** 如果对方回复较短，如何回应（比如：“简短回应，跟随对方节奏”） */
    private String shortReply;
  }

  @Data
  @Accessors(chain = true)
  public static class TopicManagement {
    /** 如果最近 3 条对话围绕同一话题，如何处理（例如：切换话题） */
    private String sameTopic;

    /** 如果用户对当前话题感兴趣，继续深化 */
    private String continueTopic;
  }

  @Data
  @Accessors(chain = true)
  public static class ToneImitation {
    /** 模仿幽默的语气 */
    private String humorous;

    /** 模仿正式的语气 */
    private String formal;

    /** 模仿敷衍的语气 */
    private String indifferent;
  }
}

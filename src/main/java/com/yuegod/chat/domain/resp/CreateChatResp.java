package com.yuegod.chat.domain.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author YueGod
 * @since 2025/2/2
 */
@Data
@Accessors(chain = true)
public class CreateChatResp {

  private String userId;

  private String content;
}

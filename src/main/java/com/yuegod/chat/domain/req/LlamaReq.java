package com.yuegod.chat.domain.req;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author YueGod
 * @since 2025/2/8
 */
@Data
@Accessors(chain = true)
public class LlamaReq {

  private String role;

  private String content;
}

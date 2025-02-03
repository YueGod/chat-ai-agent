package com.yuegod.chat.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author YueGod
 * @since 2025/2/3
 */
@Data
@Accessors(chain = true)
public class DynamicTopneResp {

  private String tone;

  private String suggestion;
}

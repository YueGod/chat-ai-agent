package com.yuegod.chat.dto;

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

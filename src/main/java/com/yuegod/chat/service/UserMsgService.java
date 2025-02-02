package com.yuegod.chat.service;

import com.yuegod.chat.db.mongo.entity.UserInfo;
import com.yuegod.chat.db.mongo.entity.UserMsg;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author YueGod
 * @since 2025/2/1
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserMsgService {
  public UserMsg getLastMsg(String userFlag) {
    return null;
  }

  public List<UserMsg> getChatHistory(String userFlag) {
    return null;
  }

  public void save(UserInfo userInfo, String resp) {}

  public List<UserMsg> getChatHistoryReply(String userFlag) {
    return null;
  }
}

package com.yuegod.chat.service;

import com.yuegod.chat.db.mongo.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author YueGod
 * @since 2025/2/1
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoService {
  public UserInfo getUserInfo(String userFlag) {
    return null;
  }

  public UserInfo createUser(String userFlag) {
    return new UserInfo();
  }
}

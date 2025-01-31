package com.yuegod.chat.db.mongo.dao;

import com.yuegod.chat.db.mongo.entity.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author YueGod
 * @since 2025/2/1
 */
@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo, String> {}

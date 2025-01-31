package com.yuegod.chat;

import lombok.Data;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author YueGod
 * @since 2025/2/1
 */
@SpringBootApplication
@EnableMongoRepositories
@EnableMongoAuditing
public class Application {}

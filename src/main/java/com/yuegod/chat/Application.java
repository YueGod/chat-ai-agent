package com.yuegod.chat;

import org.springframework.boot.SpringApplication;
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
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }

}

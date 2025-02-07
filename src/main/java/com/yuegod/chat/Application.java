package com.yuegod.chat;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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

  @Bean(destroyMethod = "close")
  public CloseableHttpClient httpClient() {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    // 最大连接数
    connectionManager.setMaxTotal(200);
    // 每个路由的最大连接数
    connectionManager.setDefaultMaxPerRoute(50);

    // 配置请求参数
    RequestConfig requestConfig =
        RequestConfig.custom()
            // 连接超时时间（毫秒）
            .setConnectTimeout(3000000)
            // 从连接池获取连接的超时时间（毫秒）
            .setConnectionRequestTimeout(3000000)
            // 请求获取数据的超时时间（毫秒）
            .setSocketTimeout(3000000)
            .build();

    // 创建 HttpClient 并配置连接池和超时
    return HttpClients.custom()
        // 设置连接池管理器
        .setConnectionManager(connectionManager)
        // 设置请求配置
        .setDefaultRequestConfig(requestConfig)
        .build();
  }
}

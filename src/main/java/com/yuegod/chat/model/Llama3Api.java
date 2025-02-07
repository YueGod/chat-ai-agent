package com.yuegod.chat.model;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.yuegod.chat.domain.req.LlamaReq;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

/**
 * @author YueGod
 * @since 2025/2/8
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class Llama3Api {

  private final CloseableHttpClient httpClient;

  private final String url = "http://127.0.0.1:5000/v1/chat/completions";

  public String call(String prompt, String message) {
    log.info("发送消息：{}", prompt);
    Map<String, Object> map = new HashMap<>();
    ArrayList<LlamaReq> list = Lists.newArrayList();
    list.add(new LlamaReq().setRole("system").setContent(prompt));
    list.add(new LlamaReq().setRole("user").setContent(message));
    map.put("messages", list);
    map.put("model", "instruct");
    String body = JSON.toJSONString(map);
    return post(body);
  }

  public String post(String body) {
    HttpPost httpPost = new HttpPost(url);
    // 设置请求头
    httpPost.setHeader("Content-Type", "application/json");
    httpPost.setHeader("Accept", "application/json");
    // 设置请求体
    StringEntity stringEntity = new StringEntity(body, ContentType.APPLICATION_JSON);
    httpPost.setEntity(stringEntity);
    try {
      CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
      return EntityUtils.toString(httpResponse.getEntity());
    } catch (Exception e) {
      log.error("请求失败", e);
    }
    return null;
  }
}

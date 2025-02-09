package com.yuegod.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ConvertToQianfan {

  public static void main(String[] args) {
    // 假设输入文件为 input.jsonl，输出文件为 output.jsonl
    // 你可以根据需要自行更改文件路径
    String inputFile = "/Users/wap/Documents/work/docs/个人文档/ft/socail_conversation.jsonl";
    String outputFile = "/Users/wap/Documents/work/docs/个人文档/ft/qianfan_ft.jsonl";

    ObjectMapper mapper = new ObjectMapper();

    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile));
        Stream<String> lines = Files.lines(Paths.get(inputFile))) {

      lines.forEach(
          line -> {
            try {
              // 解析 JSON
              JsonNode root = mapper.readTree(line);
              JsonNode messages = root.get("messages");

              if (messages != null && messages.isArray()) {
                StringBuilder userBuilder = new StringBuilder();
                StringBuilder assistantBuilder = new StringBuilder();

                // 遍历 messages 数组
                for (JsonNode msg : messages) {
                  String role = msg.get("role").asText();
                  String content = msg.get("content").asText();

                  // 根据 role 将内容加入不同的 builder
                  if ("user".equalsIgnoreCase(role)) {
                    if (userBuilder.length() > 0) {
                      userBuilder.append("\\n"); // 以换行符拼接
                    }
                    userBuilder.append(content);
                  } else if ("assistant".equalsIgnoreCase(role)) {
                    if (assistantBuilder.length() > 0) {
                      assistantBuilder.append("\\n");
                    }
                    assistantBuilder.append(content);
                  }
                }

                // 构造新的 prompt + response 对象
                String prompt = userBuilder.toString();
                String response = assistantBuilder.toString();

                // 构造输出 JSON
                String outputJson =
                    mapper.writeValueAsString(
                        mapper.createObjectNode().put("prompt", prompt).put("response", response));

                // 写出到 output.jsonl
                writer.write(outputJson);
                writer.newLine();
              }

            } catch (JsonProcessingException e) {
              System.err.println("JSON 解析异常: " + e.getMessage());
            } catch (IOException e) {
              System.err.println("IO 异常: " + e.getMessage());
            }
          });

      System.out.println("转换完成，输出文件：" + outputFile);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

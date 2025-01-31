package com.yuegod.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author YueGod
 * @since 2025/2/1
 */
@Configuration
@RequiredArgsConstructor
public class MongoConfig {

    /**
     * 注册自定义的 Converter 列表
     */
    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        // 添加读转换器（将 Date -> LocalDateTime）
        converters.add(DateToLocalDateTimeConverter.INSTANCE);
        // 添加写转换器（将 LocalDateTime -> Date）
        converters.add(LocalDateTimeToDateConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    /**
     * 读转换器: MongoDB中的Date -> LocalDateTime
     * 这里设置的时区为 "Asia/Shanghai" (UTC+8)，可以根据需要修改
     */
    @ReadingConverter
    public enum DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
        INSTANCE;

        @Override
        public LocalDateTime convert(Date source) {
            return LocalDateTime.ofInstant(source.toInstant(), ZoneId.of("Asia/Shanghai"));
        }
    }

    /**
     * 写转换器: LocalDateTime -> MongoDB中的Date
     * 同样使用 "Asia/Shanghai" 时区
     */
    @WritingConverter
    public enum LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
        INSTANCE;

        @Override
        public Date convert(LocalDateTime source) {
            return Date.from(source.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
        }
    }

}

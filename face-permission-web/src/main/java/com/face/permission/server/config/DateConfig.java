package com.face.permission.server.config;

import com.face.permission.common.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-19 14:16
 */
@Configuration
public class DateConfig {

    private static final Logger logger = LoggerUtil.COMMON_DEFAULT;

    /**
     * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                try {
                    Long timeStamp = Long.valueOf(source);
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneId.systemDefault());
                } catch (Exception e){
                    logger.warn("source not timeStamp : " + source);
                }
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            }
        };
    }
}

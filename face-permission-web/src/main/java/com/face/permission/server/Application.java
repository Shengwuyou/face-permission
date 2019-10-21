package com.face.permission.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // 配置 consul
    // 配置 Fegin  https://blog.csdn.net/wo18237095579/article/details/83348496
    // 请求MockMvc https://blog.csdn.net/wang_muhuo/article/details/84655577
}

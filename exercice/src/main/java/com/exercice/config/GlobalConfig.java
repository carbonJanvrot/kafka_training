package com.exercice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

@Configuration
public class GlobalConfig {

    @Value("${exercice.messages-per-request}")
    private int messageByRequest;

    @Bean
    public CountDownLatch countDownLatch() {
        return new CountDownLatch(messageByRequest * 2);
    }
}

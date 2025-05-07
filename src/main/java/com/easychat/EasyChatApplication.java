package com.easychat;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.easychat"})
@MapperScan(basePackages = {"com.easychat.mappers"})
@EnableTransactionManagement
@EnableScheduling
public class EasyChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyChatApplication.class, args);
    }
}

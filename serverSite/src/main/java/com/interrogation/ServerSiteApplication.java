package com.interrogation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.interrogation.mapper")
public class ServerSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerSiteApplication.class, args);
    }

}

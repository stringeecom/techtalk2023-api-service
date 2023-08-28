package com.stringee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author dautv@stringee.com on 8/12/2023
 */
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@EnableScheduling
public class VideoCallServerApp {
    public static void main(String[] args) {
        SpringApplication.run(VideoCallServerApp.class, args);
    }
}
package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.admin-id}")
    private Long adminId;

    public String getUsername() { return username; }
    public String getToken() { return token; }
    public Long getAdminId() { return adminId; }
}

package com.gongming.gmapp.controller.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "ftp")
@PropertySource("classpath:ftp.properties")
@Data
public class FtpUtils {
    private String userName;
    private String password;
}

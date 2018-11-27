package com.gongming.gmcommon.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "ftp")
@Data
public class FtpConfig {
    private String userName;
    private String password;
}

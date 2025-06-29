package com.swd.smk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vnpay")
@Data
public class VnpayProperties {
    private String tmnCode;
    private String hashSecret;
    private String returnUrl;
}

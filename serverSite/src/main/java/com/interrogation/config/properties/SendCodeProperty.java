package com.interrogation.config.properties;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.sms")
public class SendCodeProperty implements InitializingBean {

    private String regionId;
    private String templateCode;
    private String signName;
    private String keyId;
    private String keySecret;

    public static String REGION_Id;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String SIGN_NAME;
    public static String TEMPLATE_CODE;

    @Override
    public void afterPropertiesSet() throws Exception {
        REGION_Id = this.regionId;
        KEY_ID = this.keyId;
        KEY_SECRET = this.keySecret;
        SIGN_NAME = this.signName;
        TEMPLATE_CODE = this.templateCode;
    }

}

package com.planning.fcm;

import de.bytefish.fcmjava.http.options.IFcmClientSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FcmSettings implements IFcmClientSettings {
    
    @Value("${planning.fcm.appId}")
    private String appId;
    
    @Value("${planning.fcm.apiKey}")
    private String apiKey;
    
    @Value("${planning.fcm.url}")
    private String url;
    
    @Override
    public String getFcmUrl() {
        return url;
    }
    
    @Override
    public String getApiKey() {
        return this.apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}
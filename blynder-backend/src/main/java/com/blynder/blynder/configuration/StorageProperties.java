package com.blynder.blynder.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageProperties {

    private String location = "avatars";
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}

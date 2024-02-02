package com.blynder.blynder.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionInfo {
    private String room;
    private String streamerUsername;
    private String joinerUsername;

    public ConnectionInfo() {
    }
}

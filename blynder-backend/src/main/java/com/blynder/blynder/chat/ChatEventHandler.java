package com.blynder.blynder.chat;

import com.blynder.blynder.service.ChatModeratingService;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ChatEventHandler {

    @PostConstruct
    public void init() {
        server.addConnectListener(socketIOClient -> {
            socketIOClient.sendEvent("status");
        });

        server.addEventListener("text", ChatMessage.class, (client, msg, ackRequest) -> {
            if (moderatingService.isTimeouted(msg.getUsername(), client.get("streamer"))) return;
            server.getRoomOperations(msg.getRoom()).sendEvent("message", msg);
        });


        server.addEventListener("join", ConnectionInfo.class, (socketIOClient, connectionInfo, ackRequest) -> {
            if (connectionInfo.getJoinerUsername() != null) {
            socketIOClient.set("username", connectionInfo.getJoinerUsername());
            socketIOClient.set("streamer", connectionInfo.getStreamerUsername());
            }
            socketIOClient.joinRoom(connectionInfo.getRoom());
            server.getRoomOperations(connectionInfo.getRoom()).sendEvent("live_viewers", server.getRoomOperations(connectionInfo.getRoom()).getClients().size());
        });
    }

    private final SocketIOServer server;
    private final ChatModeratingService moderatingService;

    @Autowired
    public ChatEventHandler(SocketIOServer server, ChatModeratingService moderatingService) {
        this.server = server;
        this.moderatingService = moderatingService;
    }



}

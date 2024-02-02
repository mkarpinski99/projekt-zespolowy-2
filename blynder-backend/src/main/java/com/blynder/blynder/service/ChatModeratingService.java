package com.blynder.blynder.service;

import com.blynder.blynder.model.Stream;
import com.blynder.blynder.model.TimeoutInfo;
import com.blynder.blynder.model.User;
import com.blynder.blynder.repository.TimeoutInfoRepository;
import com.blynder.blynder.repository.UserRepository;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class ChatModeratingService {
    private final TimeoutInfoRepository timeoutInfoRepository;
    private final UserRepository userRepository;
    private final SocketIOServer server;

    public ChatModeratingService(TimeoutInfoRepository repository, UserRepository userRepository, SocketIOServer socketIOServer) {
        this.timeoutInfoRepository = repository;
        this.userRepository = userRepository;
        this.server = socketIOServer;
    }

    public void disconnectUserFromStream(Stream stream, String username) { // TODO tutaj ten room to bedzie secret key usera
        for (SocketIOClient client: server.getRoomOperations(stream.getStreamSecretKey()).getClients()) {
            if (client.get("username").equals(username)) {
                server.getClient(client.getSessionId()).sendEvent("forceDisconnect");
                server.getClient(client.getSessionId()).disconnect();
            }
        }
    }

    public boolean isTimeouted(String joiningUser, String streamingUser) {
        if (joiningUser == null) return false;
        User joining = userRepository.findByUsername(joiningUser).orElseThrow();
        User streamer = userRepository.findByUsername(streamingUser).orElseThrow();
        Optional<TimeoutInfo> timeoutInfo = timeoutInfoRepository.findByStreamAndTimeoutedUser(streamer.getStream(), joining);
        if (timeoutInfo.isPresent()) {
            if (timeoutInfo.get().getTimeoutedUntil().before(new Date())) {
                timeoutInfoRepository.delete(timeoutInfo.get());
                return false;
            }
            return true;
        }
        return false;
    }

    public void timeout(String username, int time, String streamerUsername) {
        User userToTimeout = userRepository.findByUsername(username).orElseThrow();
        User streamer = userRepository.findByUsername(streamerUsername).orElseThrow();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar. SECOND, (cal.get(Calendar.SECOND) + time));

        Optional<TimeoutInfo> former_timeout = timeoutInfoRepository.findByStreamAndTimeoutedUser(streamer.getStream(), userToTimeout);
        if (former_timeout.isPresent()) {
            System.out.println("tutaj bylem");
            former_timeout.get().setTimeoutedUntil(cal.getTime());
            timeoutInfoRepository.save(former_timeout.get());
            return;
        }

        TimeoutInfo timeoutInfo = new TimeoutInfo();
        timeoutInfo.setTimeoutedUser(userToTimeout);
        timeoutInfo.setStream(streamer.getStream());


        timeoutInfo.setTimeoutedUntil(cal.getTime());

        streamer.getStream().getTimeoutedUsers().add(timeoutInfo);
        userToTimeout.getUserTimeoutedIn().add(timeoutInfo);

        timeoutInfoRepository.save(timeoutInfo);
        userRepository.save(userToTimeout);
        disconnectUserFromStream(streamer.getStream(), username);
    }
}

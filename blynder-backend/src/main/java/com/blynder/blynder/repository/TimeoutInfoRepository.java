package com.blynder.blynder.repository;

import com.blynder.blynder.model.Stream;
import com.blynder.blynder.model.TimeoutInfo;
import com.blynder.blynder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeoutInfoRepository extends JpaRepository<TimeoutInfo, Integer> {
    Optional<TimeoutInfo> findByStream(Stream stream);

    Optional<TimeoutInfo> findByStreamAndTimeoutedUser(Stream stream, User user);

    Boolean existsByStreamAndTimeoutedUser(Stream stream, User user);
}

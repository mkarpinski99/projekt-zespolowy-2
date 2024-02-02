package com.blynder.blynder.repository;

import com.blynder.blynder.model.FollowInfo;
import com.blynder.blynder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowInfoRepository extends JpaRepository<FollowInfo, Integer> {
    Optional<FollowInfo> findByFollowingUserAndUserBeingFollowed(User follower, User userBeingFollowed);
}

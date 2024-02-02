package com.blynder.blynder.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
public class FollowInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @NotNull
    private long id;

    @ManyToOne
    @JoinColumn(name="following_user")
    @NotNull
    private User followingUser;

    @ManyToOne
    @JoinColumn(name="user_being_followed")
    @NotNull
    private User userBeingFollowed;

    public FollowInfo() {};

    public FollowInfo(User from, User to) {
        this.followingUser = from;
        this.userBeingFollowed = to;
    }
}

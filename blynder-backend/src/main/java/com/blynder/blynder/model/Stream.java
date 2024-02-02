package com.blynder.blynder.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "streams")
public class Stream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonProperty("user_id")
    @NotNull
    private User user;


    @JsonProperty("stream_secret_key")
    @NotNull
    private String streamSecretKey;

    @JsonProperty("stream_title")
    @NotNull
    private String streamTitle;

    @ManyToOne
    @JoinColumn(name="stream_category_id")
    @JsonProperty("stream_category")
    private StreamCategory streamCategory;

    @OneToMany(mappedBy = "stream")
    private Set<TimeoutInfo> timeoutedUsers;

    @ManyToMany(mappedBy = "moderatorOn")
    private Set<User> moderators;



}


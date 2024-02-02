package com.blynder.blynder.dto;

import com.blynder.blynder.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    @JsonProperty("stream_secret_key")
    private String streamSecretKey;

    @JsonProperty("stream_title")
    private String streamTitle;

    @JsonProperty("avatar_path")
    private String avatarPath;

    @JsonProperty("followed")
    private boolean followed;

    private boolean moderator;

    private boolean timeouted;

    private boolean banned;

    public static UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setStreamSecretKey(user.getStream().getStreamSecretKey());
        userDTO.setStreamTitle(user.getStream().getStreamTitle());
        userDTO.setAvatarPath(user.getAvatarPath());
        userDTO.setUsername(user.getUsername());
        userDTO.setBanned(user.isBanned());
        return userDTO;
    }

}

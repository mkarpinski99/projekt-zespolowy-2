package com.blynder.blynder.dto;

import com.blynder.blynder.model.Stream;
import com.blynder.blynder.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StreamDTO {

    private int id;
    @JsonProperty("user_id")
    private int userId;
    private String username;
    @JsonProperty("stream_secret_key")
    private String streamSecretKey;

    @JsonProperty("stream_title")
    private String streamTitle;

    @JsonProperty("avatar_path")
    private String avatar_Path;

    @JsonProperty("stream_category")
    private StreamCategoryDTO streamCategory;


    public static StreamDTO convertToStreamWithStreamCategoryDTO(Stream stream) {
        StreamDTO streamDTO = new StreamDTO();
        streamDTO.setId(stream.getId());
        streamDTO.setStreamSecretKey(stream.getStreamSecretKey());
        streamDTO.setStreamTitle(stream.getStreamTitle());
        streamDTO.setAvatar_Path(stream.getUser().getAvatarPath());
        streamDTO.setUserId(stream.getUser().getId());
        streamDTO.setUsername(stream.getUser().getUsername());
        if (stream.getStreamCategory() != null) {
            StreamCategoryDTO streamCategoryDTO = new StreamCategoryDTO();
            streamCategoryDTO.setCategoryName(stream.getStreamCategory().getCategoryName());
            streamCategoryDTO.setCategoryImage(stream.getStreamCategory().getCategoryImage());
            streamCategoryDTO.setId(stream.getStreamCategory().getId());
            streamDTO.setStreamCategory(streamCategoryDTO);
        }

        return streamDTO;
    }
    public static StreamDTO convertToStreamWithoutStreamCategoryDTO(Stream stream) {
        StreamDTO streamDTO = new StreamDTO();
        streamDTO.setId(stream.getId());
        streamDTO.setStreamSecretKey(stream.getStreamSecretKey());
        streamDTO.setStreamTitle(stream.getStreamTitle());
        streamDTO.setAvatar_Path(stream.getUser().getAvatarPath());
        streamDTO.setUserId(stream.getUser().getId());
        streamDTO.setUsername(stream.getUser().getUsername());
        streamDTO.setStreamCategory(null);
        return streamDTO;
    }
}


package com.blynder.blynder.dto;


import com.blynder.blynder.model.StreamCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StreamCategoryDTO {
    private int id;
    private String categoryName;
    private String categoryImage;
    private Set<StreamDTO> streams;

    public static StreamCategoryDTO convertToStreamCategoryWithStreamsDTO(StreamCategory category) {
        StreamCategoryDTO streamCategoryDTO = new StreamCategoryDTO();
        streamCategoryDTO.setCategoryImage(category.getCategoryImage());
        streamCategoryDTO.setId(category.getId());
        streamCategoryDTO.setCategoryName(category.getCategoryName());
        streamCategoryDTO.setStreams(category.getStreams().stream().map(StreamDTO::convertToStreamWithStreamCategoryDTO).collect(Collectors.toSet()));
        return streamCategoryDTO;
    }

    public static StreamCategoryDTO convertToStreamCategoryWithoutStreamsDTO(StreamCategory category) {
        StreamCategoryDTO streamCategoryDTO = new StreamCategoryDTO();
        streamCategoryDTO.setCategoryImage(category.getCategoryImage());
        streamCategoryDTO.setId(category.getId());
        streamCategoryDTO.setCategoryName(category.getCategoryName());
        streamCategoryDTO.setStreams(null);
        return streamCategoryDTO;
    }
}

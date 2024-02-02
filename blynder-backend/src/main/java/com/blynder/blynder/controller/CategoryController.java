package com.blynder.blynder.controller;

import com.blynder.blynder.dto.StreamCategoryDTO;
import com.blynder.blynder.dto.StreamDTO;
import com.blynder.blynder.model.StreamCategory;
import com.blynder.blynder.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200")
@RestController
public class CategoryController {
    private final CategoryRepository repository;





    public CategoryController(CategoryRepository repository) {
        this.repository = repository;
    }
    @GetMapping("/categories")
    List<StreamCategoryDTO> getAllCategories(
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "size", required = false) Optional<Integer> size) {
        if (page.isPresent() && size.isPresent()) {
            Pageable paging = PageRequest.of(page.get(), size.get());
            Page<StreamCategory> categories = repository.findAll(paging);
            return categories.stream().map(StreamCategoryDTO::convertToStreamCategoryWithoutStreamsDTO).collect(Collectors.toList());
        }
        List<StreamCategory> categories = repository.findAll();
        return categories.stream().map(StreamCategoryDTO::convertToStreamCategoryWithoutStreamsDTO).collect(Collectors.toList());

    }

    @GetMapping("/categories/{name}/streams")
    Set<StreamDTO> getStreamsOfCategoryByName(@PathVariable String name) {
        StreamCategory streamCategory = repository.findByCategoryName(name).orElseThrow();
        return streamCategory.getStreams().stream().map(StreamDTO::convertToStreamWithoutStreamCategoryDTO).collect(Collectors.toSet());
    }
}

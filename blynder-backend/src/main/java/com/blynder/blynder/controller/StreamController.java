package com.blynder.blynder.controller;

import com.blynder.blynder.dto.StreamCategoryDTO;
import com.blynder.blynder.dto.StreamDTO;
import com.blynder.blynder.model.Stream;
import com.blynder.blynder.model.StreamCategory;
import com.blynder.blynder.model.User;
import com.blynder.blynder.repository.CategoryRepository;
import com.blynder.blynder.repository.StreamRepository;
import com.blynder.blynder.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200")
@RestController
public class StreamController {
    private final StreamRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    public StreamController(StreamRepository repository, UserRepository userRepository,
                            CategoryRepository categoryRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/streams")
    List<StreamDTO> getAllStreams(
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "size", required = false) Optional<Integer> size) {
        if (page.isPresent() && size.isPresent()) {
            Pageable paging = PageRequest.of(page.get(), size.get());
            return  repository
                    .findAll(paging)
                    .stream()
                    .map(StreamDTO::convertToStreamWithStreamCategoryDTO)
                    .collect(Collectors.toList());
        }
        return  repository
                .findAll()
                .stream()
                .map(StreamDTO::convertToStreamWithStreamCategoryDTO)
                .collect(Collectors.toList());

    }


    @GetMapping("/streams/{id}")
    Stream getStreamById(@PathVariable int id){
        return repository.findById(id).orElseThrow();
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')"
            + "or authentication.getName().equals(#username)")
    @PutMapping("/streams/{username}/category")
    void changeCategoryOfStream(@PathVariable String username, @RequestBody StreamCategoryDTO category){

        User user = userRepository.findByUsername(username).orElseThrow();
        Stream stream = user.getStream();

        StreamCategory streamCategory = categoryRepository.findById(category.getId()).orElseThrow();
        stream.setStreamCategory(streamCategory);
        repository.save(stream);
    }
}

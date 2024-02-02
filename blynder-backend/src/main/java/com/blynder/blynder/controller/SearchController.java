package com.blynder.blynder.controller;

import com.blynder.blynder.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    private final SearchService ss;

    public SearchController(SearchService ss) {
        this.ss = ss;
    }

    @GetMapping("/search")
    public List<Object> searchForUserAndCategoryByKeyword(@RequestParam("q") String keyword) {
        return (List<Object>) ss.getUserOrCategoryBasedOnKeyword(keyword);
    }
}

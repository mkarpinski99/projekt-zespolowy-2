package com.blynder.blynder.service;


import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file, String filename);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    public String getExtension(MultipartFile file);

    void deleteAll();

    void delete(String filename) throws IOException;

    String getDownloadUrl(String file);

}

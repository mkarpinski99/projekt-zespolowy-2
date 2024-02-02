package com.blynder.blynder.service;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
public class IndexingService {

    private final EntityManager em;

    IndexingService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void initiateIndexing() throws InterruptedException {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        fullTextEntityManager.createIndexer().startAndWait();
    }
}

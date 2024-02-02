package com.blynder.blynder.service;

import com.blynder.blynder.dto.StreamCategoryDTO;
import com.blynder.blynder.dto.UserDTO;
import com.blynder.blynder.model.StreamCategory;
import com.blynder.blynder.model.User;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final EntityManager entityManager;

    public SearchService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Object getUserOrCategoryBasedOnKeyword(String keyword){
        FullTextEntityManager fullTextEntityManager =
                Search.getFullTextEntityManager(entityManager);

        QueryBuilder userQueryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
        QueryBuilder categoryQueryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(StreamCategory.class).get();


        Query combinedQuery = userQueryBuilder
                .bool()
                .should(categoryQueryBuilder.phrase()
                        .withSlop(1)
                        .boostedTo(5)
                        .onField( "categoryName" )
                        .sentence(keyword)
                        .createQuery())
                .should(userQueryBuilder.phrase()
                        .withSlop(1)
                        .boostedTo(5)
                        .onField( "username" )
                        .sentence(keyword)
                        .createQuery())
                .createQuery();


        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(combinedQuery, User.class, StreamCategory.class);


        //noinspection unchecked
        return fullTextQuery.getResultList().stream().map(o -> {
            if (o instanceof User) {
                return UserDTO.convertToUserDTO((User) o);
            }
            else return StreamCategoryDTO.convertToStreamCategoryWithoutStreamsDTO((StreamCategory) o);
        }).collect(Collectors.toList()); //TODO paginacja wynikow
    }


}

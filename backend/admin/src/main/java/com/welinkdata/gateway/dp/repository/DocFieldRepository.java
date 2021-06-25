package com.welinkdata.gateway.dp.repository;

import com.welinkdata.gateway.dp.domain.DocField;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DocFieldRepository extends MongoRepository<DocField, String> {

    @Query(sort = "{ orderNum : 1 }")
    List<DocField> findByDocPath(String docPath);

    Long deleteDocFieldByDocPath(String docPath);
}

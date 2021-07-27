package com.welinkdata.gateway.bizTP.repository;



import com.welinkdata.gateway.bizTP.domain.TPType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TPTypeRepository extends MongoRepository<TPType, String> {

    List<TPType> findAllByParentId(String parentId);
}

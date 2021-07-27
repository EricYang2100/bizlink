package com.welinkdata.gateway.bizCell.repository;

import com.welinkdata.gateway.bizCell.domain.DocAttribute;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocAttributeRepository extends MongoRepository<DocAttribute, String> {

    DocAttribute findFirstByDocPath(String docPath);
}

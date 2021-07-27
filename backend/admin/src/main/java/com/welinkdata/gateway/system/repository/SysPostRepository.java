package com.welinkdata.gateway.system.repository;


import com.welinkdata.gateway.common.core.domain.entity.SysPost;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysPostRepository extends MongoRepository<SysPost, String> {
    SysPost findByPostName(String postName);

    SysPost findByPostCode(String postCode);
}

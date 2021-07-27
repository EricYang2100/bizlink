package com.welinkdata.gateway.bizTP.repository;

import com.welinkdata.gateway.bizTP.domain.TPInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TPInfoRepository extends MongoRepository<TPInfo, String> {

    List<TPInfo> findAllByTpTypeId(String typeId);

}


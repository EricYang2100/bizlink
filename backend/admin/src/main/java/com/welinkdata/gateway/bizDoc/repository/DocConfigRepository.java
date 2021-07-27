package com.welinkdata.gateway.bizDoc.repository;

import com.welinkdata.gateway.bizDoc.domain.DocConfig;
import com.welinkdata.gateway.system.domain.SysConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocConfigRepository extends MongoRepository<DocConfig, String> {

    DocConfig findByDocSysName(String configKey);

    List<DocConfig> findAllByShareType(String shareType);
}
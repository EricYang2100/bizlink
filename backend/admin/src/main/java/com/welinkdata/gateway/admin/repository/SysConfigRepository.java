package com.welinkdata.gateway.admin.repository;

import com.welinkdata.gateway.admin.domain.SysConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysConfigRepository extends MongoRepository<SysConfig, String> {

    SysConfig findByConfigKey(String configKey);
}

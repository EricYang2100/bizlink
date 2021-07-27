package com.welinkdata.gateway.system.repository;

import com.welinkdata.gateway.system.domain.SysConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysConfigRepository extends MongoRepository<SysConfig, String> {

    SysConfig findByConfigKey(String configKey);
}

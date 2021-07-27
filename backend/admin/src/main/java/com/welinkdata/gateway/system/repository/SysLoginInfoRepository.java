package com.welinkdata.gateway.system.repository;


import com.welinkdata.gateway.system.domain.SysLoginInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysLoginInfoRepository extends MongoRepository<SysLoginInfo, String> {
}

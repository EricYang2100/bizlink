package com.welinkdata.gateway.admin.repository;


import com.welinkdata.gateway.admin.domain.SysLoginInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysLoginInfoRepository extends MongoRepository<SysLoginInfo, String> {
}

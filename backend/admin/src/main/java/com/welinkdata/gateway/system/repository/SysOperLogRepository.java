package com.welinkdata.gateway.system.repository;


import com.welinkdata.gateway.system.domain.SysOperLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysOperLogRepository extends MongoRepository<SysOperLog, String> {
}

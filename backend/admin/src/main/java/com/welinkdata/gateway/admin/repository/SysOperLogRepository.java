package com.welinkdata.gateway.admin.repository;


import com.welinkdata.gateway.admin.domain.SysOperLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysOperLogRepository extends MongoRepository<SysOperLog, String> {
}

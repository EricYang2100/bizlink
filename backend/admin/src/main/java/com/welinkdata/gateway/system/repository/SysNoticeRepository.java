package com.welinkdata.gateway.system.repository;

import com.welinkdata.gateway.system.domain.SysNotice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysNoticeRepository extends MongoRepository<SysNotice, String> {
}

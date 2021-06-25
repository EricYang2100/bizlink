package com.welinkdata.gateway.admin.repository;

import com.welinkdata.gateway.admin.domain.SysNotice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysNoticeRepository extends MongoRepository<SysNotice, String> {
}

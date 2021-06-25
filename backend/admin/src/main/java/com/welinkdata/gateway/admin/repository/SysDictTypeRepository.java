package com.welinkdata.gateway.admin.repository;


import com.welinkdata.gateway.common.core.domain.entity.SysDictType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SysDictTypeRepository extends MongoRepository<SysDictType, String> {
    //List<SysDictType> findByDelFlag(String delFlag);
    SysDictType findFirstByDictType(String dictType);
}

package com.welinkdata.gateway.system.repository;


import com.welinkdata.gateway.common.core.domain.entity.SysDictType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysDictTypeRepository extends MongoRepository<SysDictType, String> {
    //List<SysDictType> findByDelFlag(String delFlag);
    SysDictType findFirstByDictType(String dictType);
}

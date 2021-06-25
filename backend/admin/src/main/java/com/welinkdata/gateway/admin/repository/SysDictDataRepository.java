package com.welinkdata.gateway.admin.repository;

import com.welinkdata.gateway.common.core.domain.entity.SysDictData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SysDictDataRepository extends MongoRepository<SysDictData, String> {

    @Query(sort = "{ dictSort : 1 }")
    List<SysDictData> findByDictType(String dictType);

    SysDictData findByDictTypeAndDictValue(String dictType,String dictValue);

    Long deleteSysDictDataByDictType(String dictType);
}

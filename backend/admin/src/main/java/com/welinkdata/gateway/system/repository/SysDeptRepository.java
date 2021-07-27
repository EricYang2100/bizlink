package com.welinkdata.gateway.system.repository;

import com.welinkdata.gateway.common.core.domain.entity.SysDept;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SysDeptRepository extends MongoRepository<SysDept, String>{


    SysDept findByDeptName(String deptName);

    List<SysDept> findAllByParentId(String parentId);
}

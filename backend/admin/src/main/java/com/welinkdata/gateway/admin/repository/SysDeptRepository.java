package com.welinkdata.gateway.admin.repository;

import com.welinkdata.gateway.common.core.domain.entity.SysDept;
import com.welinkdata.gateway.common.core.domain.entity.SysMenu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SysDeptRepository extends MongoRepository<SysDept, String>{


    SysDept findByDeptName(String deptName);

    List<SysDept> findAllByParentId(String parentId);
}

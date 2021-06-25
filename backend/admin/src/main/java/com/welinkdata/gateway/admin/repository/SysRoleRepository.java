package com.welinkdata.gateway.admin.repository;

import com.welinkdata.gateway.common.core.domain.entity.SysRole;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysRoleRepository extends MongoRepository<SysRole, String> {
    SysRole findByRoleKey(String roleKey);
    SysRole findByRoleName(String roleName);
}

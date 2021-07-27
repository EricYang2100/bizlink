package com.welinkdata.gateway.system.repository;


import com.welinkdata.gateway.common.core.domain.entity.SysUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SysUserRepository extends MongoRepository<SysUser, String> {
    SysUser findByUserName(String userName);
    SysUser findByPhoneNumber(String phoneNumber);
    SysUser findByEmail(String email);
    List<SysUser> findAllByDeptId(String deptId);
}

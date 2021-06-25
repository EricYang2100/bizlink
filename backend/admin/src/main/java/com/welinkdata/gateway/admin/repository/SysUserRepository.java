package com.welinkdata.gateway.admin.repository;


import com.welinkdata.gateway.common.core.domain.entity.SysUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysUserRepository extends MongoRepository<SysUser, String> {
    SysUser findByUserName(String userName);
    SysUser findByPhoneNumber(String phoneNumber);
    SysUser findByEmail(String email);
}

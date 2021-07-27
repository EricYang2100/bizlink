package com.welinkdata.gateway.system.repository;

import com.welinkdata.gateway.common.core.domain.entity.SysMenu;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SysMenuRepository extends MongoRepository<SysMenu, String> {
    SysMenu findByMenuName(String menuName);
}

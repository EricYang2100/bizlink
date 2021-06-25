package com.welinkdata.gateway.common.core.domain.entity;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "sys_role")
public class SysRole  extends MongoEntity {


    /** 角色ID */
    @Id
    private String roleId;

    /** 角色名称 */

    private String roleName;

    /** 角色权限 */
    private String roleKey;

    /** 角色排序 */
    private Long roleSort;

    /** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限） */
    private String dataScope;

    /** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
    private boolean menuCheckStrictly;

    /** 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ） */
    private boolean deptCheckStrictly;

    /** 角色状态（0正常 1停用） */
    private String status;




    /** 菜单组 */
    private String[] menuIds;

    /** 部门组（数据权限） */
    private String[] deptIds;

    private String createBy;

    private String updateBy;

    public boolean isAdmin() {

        return "admin".equals(roleKey);
    }
}

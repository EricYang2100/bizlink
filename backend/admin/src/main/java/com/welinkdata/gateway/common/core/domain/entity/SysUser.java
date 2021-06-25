package com.welinkdata.gateway.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@ToString
@Document(collection = "sys_user")
public class SysUser extends MongoEntity {

    @Id
    private String userId;

    /** 用户账号 */
    private String userName;

    /** 用户名称 */
    private String nickName;

    /** 用户邮箱 */
    private String email;

    /** 手机号码 */
    private String phoneNumber;

    /** 用户性别 0=男,1=女,2=未知 */
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 密码 */
    private String password;

    /** 帐号状态（0正常 1停用） */
    private String status;

    private boolean isAdmin;



    /** 最后登录IP */
    private String loginIp;

    /** 最后登录时间 */
    @JsonFormat(timezone= "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;

    /** 所属部门 */
    private String deptId;


    /** 角色组 */
    private String[] roleIds;

    /** 岗位组 */
    private String[] postIds;

    private String createBy;
    private String updateBy;

}

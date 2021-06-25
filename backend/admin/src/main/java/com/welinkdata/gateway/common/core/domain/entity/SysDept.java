package com.welinkdata.gateway.common.core.domain.entity;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Document(collection = "sys_dept")
public class SysDept extends MongoEntity {


    /** 部门ID */
    @Id
    private String deptId;

    /** 父部门ID */
    private String parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 部门名称 */
    private String deptName;

    /** 显示顺序 */
    private String orderNum;

    /** 负责人 */
    private String leader;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 部门状态:0正常,1停用 */
    private String status;

     /** 父部门名称 */
    private String parentName;

    private String createBy;

    private String updateBy;

    @Transient
    private List<SysDept> children=new ArrayList<>();
}

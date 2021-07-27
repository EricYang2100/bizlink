package com.welinkdata.gateway.bizTP.domain;


import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;


@Data
@ToString
@Document(collection = "bizTP_TPInfo")

/**
 * 合作伙伴信息
 */
public class TPInfo extends MongoEntity {

    @Id
    private String id;

    /** 合作伙伴名称 */
    private String tpName;


    /** 合作伙伴编码 */
    private String tpCode;


    /** TPType  */
    private String tpTypeId;

    /** 冗余合作伙伴分类全路径：便于按照类型查找   */
    private String tpTypeFullPath;

    /** 所属城市  */
    private String city;
    private String[] cityOptions;

    /** 详细地址  */
    private String detailAddr;

    /** 联系人  */
    private String contactName;

    /** 联系人电话  */
    private String contactPhone;

    /** TP状态（0正常 1停用） */
    private String status;

    /** 邀请状态（0 待邀请 1邀请中 2 已激活未连接 3. 本周有连接 4. 连接中） */
    private String inviteStatus;

    /** 邀请时间，如已激活为激活时间 */
    private Date lastInventDate;

    /** 所属部门 */
    private String deptId;

    /** 冗余部门全路径：便于查找   */
    private String deptFullPath;

    /** 业务负责人ID  */
    private String[] tpMgr;

    /** 标签 */
    private String[] tpTag;

    /** 测试字段 */
    private BigDecimal  testMoney;

}

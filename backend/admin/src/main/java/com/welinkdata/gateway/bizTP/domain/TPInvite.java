package com.welinkdata.gateway.bizTP.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@ToString
@Document(collection = "bizTP_TPInvite")
/**
 * 给合作伙伴发送的邀请
 */
public class TPInvite extends MongoEntity {
    @Id
    private String id;

    /** 合作伙伴ID */
    private String tpId;

    /** 邀请状态（0正常 1停用  2. 已使用） */
    private String status;

    /** 邀请说明 */
    private String inviteComment;

    /** 邀请时间 */
    @JsonFormat(timezone= "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inviteTime;

    /** 创建人 */
    private String createBy;

    /** 失效时间 */
    @JsonFormat(timezone= "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /** 使用时间 */
    @JsonFormat(timezone= "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date linkTime;
}

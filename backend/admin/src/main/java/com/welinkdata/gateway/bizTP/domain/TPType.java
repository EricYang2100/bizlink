package com.welinkdata.gateway.bizTP.domain;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "bizTP_TPType")
public class TPType extends MongoEntity {

    @Id
    private String id;

    /** 分类名称 */
    private String typeName;

    /** 父ID */
    private String parentId;

    /** 显示顺序 */
    private Integer orderNum;

    /** 树结构从头到尾的ID用“-” 隔开组成的全路径 */
    private String ancestors;

    /** 分类状态（0正常 1停用） */
    private String status;
}

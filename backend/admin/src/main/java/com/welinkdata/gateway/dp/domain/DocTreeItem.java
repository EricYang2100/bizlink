package com.welinkdata.gateway.dp.domain;


import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "bizdoc_doc_tree_item")

/**
 * 文档定义的目录树
 */
public class DocTreeItem extends MongoEntity {

    @Id
    private String id;

    /** 文档类型编码-英文名称 */
    private String code;

    /** 文档名称-中文名称 */
    private String name;

    /** 文档类型-
     *  系统字典： sysDocType
     * dir          -- 目录,
     * doc-base     -- 一般文档，
     * doc-share    -- 共享文档
     * doc-exchange -- 交换文档
     * doc-sync     -- 同步信息
     * service      -- 服务（待细分）
     * 等
     * */
    private String docType;

    /** 系统缺省（0缺省 1用户自定义） */
    private String sysDefault;

    /** 父ID */
    private String parentId;

    /** 显示顺序 */
    private Integer orderNum;

    /** 祖级列表 --使用code 来组成*/
    private String docPath;

    /** 状态（0正常 1停用） */
    private String status;
}

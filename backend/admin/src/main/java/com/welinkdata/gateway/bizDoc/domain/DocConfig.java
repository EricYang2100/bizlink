package com.welinkdata.gateway.bizDoc.domain;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "bizDoc_docConfig")

/**
 * 文档配置
 */
public class DocConfig extends MongoEntity {

    @Id
    private String  id;

    /** 文档英文名称 */
    private String docSysName;

    /** 文档中文名称 */
    private String  docName;

    /** 文档协同方式： docShare，docExchange, docApp */
    private String  shareType;

    /** 文档字段定义路径 */
    private String  docFieldPath;

    /** 文档模板定义路径， null 为未定义 */
    private String  docTemplatePath;

    /** 状态（0正常 1禁用） */
    private String status;

}

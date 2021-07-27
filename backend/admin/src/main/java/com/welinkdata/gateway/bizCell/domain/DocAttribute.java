package com.welinkdata.gateway.bizCell.domain;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * 文档属性定义
 */
@Data
@ToString
@Document(collection = "bizCell_docField")
public class DocAttribute extends MongoEntity {

    @Id
    String id;

    /**
     *  文档路径定义
     */
    String docPath;

    /** 用于key 自增长*/
    Long  maxId;


    List<DocAttributeField> fieldList = new ArrayList<>();
}

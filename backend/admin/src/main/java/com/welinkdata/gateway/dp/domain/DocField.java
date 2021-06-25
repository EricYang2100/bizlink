package com.welinkdata.gateway.dp.domain;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "bizdoc_doc_field")
/**
 * 产品字段模板明细
 */
public class DocField extends MongoEntity {

    @Id
    private String id;

    /** docDefine ancestors+Code */
    private String docPath;

    /** 数据库列名 */
    private String sysName;


    /** 字段中文名称 */
    private String fieldName;

    /** 列类型 */
    private String columnType;

    /** 系统必备（0:不可停用 1可停用） */
    private String sysRequired;

    /** 状态（0正常 1停用） */
    private String status;

    /** 是否必填（1是） */
    private String isRequired;

    /** 是否列表字段（1是） */
    private String isList;

    /** 是否查询字段（1是） */
    private String isQuery;

    /** 查询方式（等于、不等于、大于、小于、范围, 左匹配） */
    private String queryType;

    /** 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件） */
    private String htmlType;

    /** 字典类型 */
    private String dictType;

    /** 显示排序 */
    private Long orderNum;

    /** 父ID */
    private String parentId;

}

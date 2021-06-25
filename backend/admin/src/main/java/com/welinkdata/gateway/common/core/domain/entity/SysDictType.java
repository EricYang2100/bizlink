package com.welinkdata.gateway.common.core.domain.entity;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "sys_dict_type")

/**
 * 系统数据字典-类型
 */
public class SysDictType extends MongoEntity {
    @Id
    private String  id;

    /** 字典名称--中文名称 */
    private String dictName;

    /** 字典类型--英文名称 */
    private String dictType;

    /** 状态（0正常 1停用） */
    private String status;

}

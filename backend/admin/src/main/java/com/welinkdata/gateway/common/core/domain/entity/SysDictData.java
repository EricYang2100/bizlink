package com.welinkdata.gateway.common.core.domain.entity;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "sys_dict_data")


/**
 * 系统数据字典-数据
 */
public class SysDictData extends MongoEntity {
    @Id
    private String  id;

    /** 字典排序 */
    private     Long dictSort;

    /** 字典标签 */
    private     String dictLabel;

    /** 字典键值 */
    private     String dictValue;

    /** 字典类型 */
    private     String dictType;

    /** 是否默认（Y是 N否） */
    private String isDefault;

    /** 状态（0正常 1停用） */
    private String status;

}

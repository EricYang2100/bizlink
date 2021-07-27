package com.welinkdata.gateway.bizCell.domain;


import lombok.Data;
import lombok.ToString;


/**
 *  文档字段-字典内容
 */
@Data
@ToString
public class DocAttributeDictData {

    private     Long dictSort;

    /** 字典标签 */
    private     String dictLabel;

    /** 字典键值 */
    private     String dictValue;

    /** 是否默认（Y是 N否） */
    private String isDefault;
}

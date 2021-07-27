package com.welinkdata.gateway.common.core.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 提供树状结构处理
 */
@Data
@ToString
public class DictEntity implements Serializable {

    private String dictLabel;     //名称
    private String dictValue;     // id
   // private String ancestors;     // 祖级目录

    public DictEntity(){

    }

    public DictEntity(String dictLabel, String dictValue){

        this.dictLabel = dictLabel;
        this.dictValue = dictValue;
        //this.ancestors = ancestors;
    }
}

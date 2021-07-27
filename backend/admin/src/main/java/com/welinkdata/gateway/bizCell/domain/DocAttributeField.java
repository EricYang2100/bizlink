package com.welinkdata.gateway.bizCell.domain;

import com.welinkdata.gateway.common.constant.DocFieldConstants;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString

/**
 * 文档属性明细定义
 */
public class DocAttributeField {

    
    private String  key;

    /** 数据库列名 */
    private String sysName;


    /** 字段中文名称 */
    private String title;

    /** 字段类型
     *  参考 common.constants.DocFieldConstants
     * */
    private String dataType;

    /** 系统定义-不可删除（0:不是 1是-可选 2 是必须） */
    private String sysDefine;

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



    /** 字典项设置
     *   0: 无字典
     *   1：系统字典
     *   2：内置字典
     *   3: 内置数组（只用dictValue，不用dictLabel）
     */
    private String dictSetting;

    /** 系统数据字典类型 */
    private String dictType;

    /** 内置数据字典类型
     外部查询文档定义时，自动包含系统数据字典的内容
     */
    private List<DocAttributeDictData> optionList=new ArrayList<>();

    /**
     * 子字段。
     */
    private List<DocAttributeField> children = new ArrayList<>();


    /**
     * 判断是否有子字段
     * @return
     */
    public boolean hasChild(){
        if(DocFieldConstants.RECORD.equals(dataType)) return true;
        if(DocFieldConstants.LIST.equals(dataType)) return true;
        return false;
    }

}

package com.welinkdata.gateway.common.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MongoEntity {

    public static final String NOR_FLAG="0";
    public static final String DEL_FLAG="2";

    /** 更新时间 */
    @JsonFormat(timezone= "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(timezone= "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @LastModifiedDate
    private Date updateTime;

    /** 备注 */
    private String remark;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 请求参数 */
    @Transient
    private Map<String, Object> params;

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }
}

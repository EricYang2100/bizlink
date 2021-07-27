package com.welinkdata.gateway.bizDoc.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

/**
 * 作废，暂时保留
 */
public class MongoDyncEntity extends Document {

    /** 更新时间 */
    @JsonFormat(timezone= "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(timezone= "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @LastModifiedDate
    private Date updateTime;

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

}

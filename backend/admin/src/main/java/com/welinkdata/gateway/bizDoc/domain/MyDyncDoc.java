package com.welinkdata.gateway.bizDoc.domain;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import com.welinkdata.gateway.common.utils.StringUtils;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Data
@ToString
@Document(collection = "bizDoc_dyncDoc")
public class MyDyncDoc extends MongoEntity {

    @Id
    private String id;

    /** 文档定义路径 */
    private String docSysName;

    /** 动态属性字段存放的HashMap */
    private HashMap f = new HashMap();

    public String getFieldStringValue(String key){
        Object o = f.get(key);
        if(StringUtils.isNotNull(o)){
            if(o instanceof String){
                return (String)o;
            }
        }
        return null;
    }

    public Date getFieldDateValue(String key){
        Object o = f.get(key);
        if(StringUtils.isNotNull(o)){
            if(o instanceof Date){
                return (Date)o;
            }
            if( o instanceof String){
                String s = (String)o;
                try {
                    return SimpleDateFormat.getInstance().parse(s);
                } catch (ParseException e) {
                    return null;
                }
            }
        }
        return null;
    }

    public Long getFieldLongValue(String key){
        Object o = f.get(key);
        if(StringUtils.isNotNull(o)){
            if(o instanceof Long){
                return (Long)o;
            }
            if( o instanceof Number){
                Number s = (Number)o;
                return s.longValue();
            }
            if( o instanceof String){
               return  Long.parseLong((String)o);
            }

        }
        return null;
    }

    public Double getFieldDoubleValue(String key){
        Object o = f.get(key);
        if(StringUtils.isNotNull(o)){
            if(o instanceof Double){
                return (Double)o;
            }
            if( o instanceof Number){
                Number s = (Number)o;
                return s.doubleValue();
            }
            if( o instanceof String){
                return  Double.parseDouble((String)o);
            }


        }
        return null;
    }
}

